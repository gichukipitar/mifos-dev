package com.fineract.mifos.mifos_core.commands.service;

import com.fineract.mifos.mifos_core.batch.exception.ErrorInfo;
import com.fineract.mifos.mifos_core.commands.dtos.CommandProcessingResultType;
import com.fineract.mifos.mifos_core.commands.dtos.CommandWrapper;
import com.fineract.mifos.mifos_core.commands.entity.CommandSource;
import com.fineract.mifos.mifos_core.commands.exception.UnsupportedCommandException;
import com.fineract.mifos.mifos_core.commands.handler.NewCommandSourceHandler;
import com.fineract.mifos.mifos_core.infrastructure.configuration.service.ConfigurationDomainService;
import com.fineract.mifos.mifos_core.infrastructure.core.api.JsonCommand;
import com.fineract.mifos.mifos_core.infrastructure.core.data.CommandProcessingResult;
import com.fineract.mifos.mifos_core.infrastructure.core.domain.BatchRequestContextHolder;
import com.fineract.mifos.mifos_core.infrastructure.core.dto.FineractRequestContextHolder;
import com.fineract.mifos.mifos_core.infrastructure.core.exception.ErrorHandler;
import com.fineract.mifos.mifos_core.infrastructure.core.exception.IdempotentCommandProcessFailedException;
import com.fineract.mifos.mifos_core.infrastructure.core.exception.IdempotentCommandProcessSucceedException;
import com.fineract.mifos.mifos_core.infrastructure.core.exception.PlatformApiDataValidationException;
import com.fineract.mifos.mifos_core.infrastructure.core.serialization.GoogleGsonSerializerHelper;
import com.fineract.mifos.mifos_core.infrastructure.core.serialization.ToApiJsonSerializer;
import com.fineract.mifos.mifos_core.infrastructure.core.service.ThreadLocalContextUtil;
import com.fineract.mifos.mifos_core.infrastructure.security.service.PlatformSecurityContext;
import com.fineract.mifos.mifos_core.useradministration.entity.AppUser;
import com.google.gson.Gson;
import io.github.resilience4j.retry.Retry.*;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.fineract.mifos.mifos_core.commands.dtos.CommandProcessingResultType.ERROR;
import static com.fineract.mifos.mifos_core.commands.dtos.CommandProcessingResultType.PROCESSED;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;

@Service
@Slf4j
@RequiredArgsConstructor
public class SynchronousCommandProcessingService implements CommandProcessingService {

    public static final String IDEMPOTENCY_KEY_STORE_FLAG = "idempotencyKeyStoreFlag";

    public static final String IDEMPOTENCY_KEY_ATTRIBUTE = "IdempotencyKeyAttribute";
    public static final String COMMAND_SOURCE_ID = "commandSourceId";
    private final PlatformSecurityContext context;
    private final ApplicationContext applicationContext;
    private final ToApiJsonSerializer<Map<String, Object>> toApiJsonSerializer;
    private final ToApiJsonSerializer<CommandProcessingResult> toApiResultJsonSerializer;
    private final ConfigurationDomainService configurationDomainService;
    private final CommandHandlerProvider commandHandlerProvider;
    private final IdempotencyKeyResolver idempotencyKeyResolver;
    private final CommandSourceService commandSourceService;

    private final FineractRequestContextHolder fineractRequestContextHolder;
    private final Gson gson = GoogleGsonSerializerHelper.createSimpleGson();

    @Override
    @Retry (name = "executeCommand", fallbackMethod = "fallbackExecuteCommand")
    public CommandProcessingResult executeCommand(final CommandWrapper wrapper, final JsonCommand command,
                                                  final boolean isApprovedByChecker) {
        // Do not store the idempotency key because of the exception handling
        setIdempotencyKeyStoreFlag(false);

        Long commandId = (Long) fineractRequestContextHolder.getAttribute(COMMAND_SOURCE_ID, null);
        boolean isRetry = commandId != null;
        boolean isEnclosingTransaction = BatchRequestContextHolder.isEnclosingTransaction();

        CommandSource commandSource = null;
        String idempotencyKey;
        if (isRetry) {
            commandSource = commandSourceService.getCommandSource(commandId);
            idempotencyKey = commandSource.getIdempotencyKey();
        } else if ((commandId = command.commandId()) != null) { // action on the command itself
            commandSource = commandSourceService.getCommandSource(commandId);
            idempotencyKey = commandSource.getIdempotencyKey();
        } else {
            idempotencyKey = idempotencyKeyResolver.resolve(wrapper);
        }
        exceptionWhenTheRequestAlreadyProcessed(wrapper, idempotencyKey, isRetry);

        AppUser user = context.authenticatedUser(wrapper);
        if (commandSource == null) {
            if (isEnclosingTransaction) {
                commandSource = commandSourceService.getInitialCommandSource(wrapper, command, user, idempotencyKey);
            } else {
                commandSource = commandSourceService.saveInitialNewTransaction(wrapper, command, user, idempotencyKey);
                commandId = commandSource.getId();
            }
        }
        if (commandId != null) {
            storeCommandIdInContext(commandSource); // Store command id as a request attribute
        }

        boolean isMakerChecker = configurationDomainService.isMakerCheckerEnabledForTask(wrapper.taskPermissionName());
        if (isApprovedByChecker || (isMakerChecker && user.isCheckerSuperUser())) {
            commandSource.markAsChecked(user);
        }
        setIdempotencyKeyStoreFlag(true);

        final CommandProcessingResult result;
        try {
            result = commandSourceService.processCommand(findCommandHandler(wrapper), command, commandSource, user, isApprovedByChecker,
                    isMakerChecker);
        } catch (Throwable t) { // NOSONAR
            RuntimeException mappable = ErrorHandler.getMappable(t);
            ErrorInfo errorInfo = commandSourceService.generateErrorInfo(mappable);
            Integer statusCode = errorInfo.getStatusCode();
            commandSource.setResultStatusCode(statusCode);
            commandSource.setResult(errorInfo.getMessage());
            if (statusCode != SC_OK) {
                commandSource.setStatus(ERROR);
            }
            if (!isEnclosingTransaction) { // TODO: temporary solution
                commandSource = commandSourceService.saveResultNewTransaction(commandSource);
            }
            // must not throw any exception; must persist in new transaction as the current transaction was already
            // marked as rollback
            publishHookErrorEvent(wrapper, command, errorInfo);
            throw mappable;
        }

        commandSource.setResultStatusCode(SC_OK);
        commandSource.updateForAudit(result);
        commandSource.setResult(toApiJsonSerializer.serializeResult(result));
        commandSource.setStatus(PROCESSED);
        commandSource = commandSourceService.saveResultSameTransaction(commandSource);
        storeCommandIdInContext(commandSource); // Store command id as a request attribute

        result.setRollbackTransaction(null);
        publishHookEvent(wrapper.entityName(), wrapper.actionName(), command, result); // TODO must be performed in a
        // new transaction
        return result;
    }

    private void storeCommandIdInContext(CommandSource savedCommandSource) {
        if (savedCommandSource.getId() == null) {
            throw new IllegalStateException("Command source not saved");
        }
        // Idempotency filters and retry need this
        fineractRequestContextHolder.setAttribute(COMMAND_SOURCE_ID, savedCommandSource.getId());
    }

    private void publishHookErrorEvent(CommandWrapper wrapper, JsonCommand command, ErrorInfo errorInfo) {
        publishHookEvent(wrapper.entityName(), wrapper.actionName(), command, gson.toJson(errorInfo));
    }

    private void exceptionWhenTheRequestAlreadyProcessed(CommandWrapper wrapper, String idempotencyKey, boolean retry) {
        CommandSource command = commandSourceService.findCommandSource(wrapper, idempotencyKey);
        if (command == null) {
            return;
        }
        CommandProcessingResultType status = CommandProcessingResultType.fromInt(command.getStatus());
        switch (status) {
            case UNDER_PROCESSING -> throw new IdempotentCommandProcessUnderProcessingException(wrapper, idempotencyKey);
            case PROCESSED -> throw new IdempotentCommandProcessSucceedException(wrapper, idempotencyKey, command);
            case ERROR -> {
                if (!retry) {
                    throw new IdempotentCommandProcessFailedException(wrapper, idempotencyKey, command);
                }
            }
            default -> {
            }
        }
    }

    private void setIdempotencyKeyStoreFlag(boolean flag) {
        fineractRequestContextHolder.setAttribute(IDEMPOTENCY_KEY_STORE_FLAG, flag);
    }

    @SuppressWarnings("unused")
    public CommandProcessingResult fallbackExecuteCommand(Exception e) {
        throw ErrorHandler.getMappable(e);
    }

    private NewCommandSourceHandler findCommandHandler(final CommandWrapper wrapper) {
        NewCommandSourceHandler handler;

        if (wrapper.isDatatableResource()) {
            if (wrapper.isCreateDatatable()) {
                handler = applicationContext.getBean("createDatatableCommandHandler", NewCommandSourceHandler.class);
            } else if (wrapper.isDeleteDatatable()) {
                handler = applicationContext.getBean("deleteDatatableCommandHandler", NewCommandSourceHandler.class);
            } else if (wrapper.isUpdateDatatable()) {
                handler = applicationContext.getBean("updateDatatableCommandHandler", NewCommandSourceHandler.class);
            } else if (wrapper.isCreate()) {
                handler = applicationContext.getBean("createDatatableEntryCommandHandler", NewCommandSourceHandler.class);
            } else if (wrapper.isUpdateMultiple()) {
                handler = applicationContext.getBean("updateOneToManyDatatableEntryCommandHandler", NewCommandSourceHandler.class);
            } else if (wrapper.isUpdateOneToOne()) {
                handler = applicationContext.getBean("updateOneToOneDatatableEntryCommandHandler", NewCommandSourceHandler.class);
            } else if (wrapper.isDeleteMultiple()) {
                handler = applicationContext.getBean("deleteOneToManyDatatableEntryCommandHandler", NewCommandSourceHandler.class);
            } else if (wrapper.isDeleteOneToOne()) {
                handler = applicationContext.getBean("deleteOneToOneDatatableEntryCommandHandler", NewCommandSourceHandler.class);
            } else if (wrapper.isRegisterDatatable()) {
                handler = applicationContext.getBean("registerDatatableCommandHandler", NewCommandSourceHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isNoteResource()) {
            if (wrapper.isCreate()) {
                handler = applicationContext.getBean("createNoteCommandHandler", NewCommandSourceHandler.class);
            } else if (wrapper.isUpdate()) {
                handler = applicationContext.getBean("updateNoteCommandHandler", NewCommandSourceHandler.class);
            } else if (wrapper.isDelete()) {
                handler = applicationContext.getBean("deleteNoteCommandHandler", NewCommandSourceHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isSurveyResource()) {
            if (wrapper.isRegisterSurvey()) {
                handler = applicationContext.getBean("registerSurveyCommandHandler", NewCommandSourceHandler.class);
            } else if (wrapper.isFullFilSurvey()) {
                handler = applicationContext.getBean("fullFilSurveyCommandHandler", NewCommandSourceHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isLoanDisburseDetailResource()) {
            if (wrapper.isUpdateDisbursementDate()) {
                handler = applicationContext.getBean("updateLoanDisburseDateCommandHandler", NewCommandSourceHandler.class);
            } else if (wrapper.addAndDeleteDisbursementDetails()) {
                handler = applicationContext.getBean("addAndDeleteLoanDisburseDetailsCommandHandler", NewCommandSourceHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else {
            handler = commandHandlerProvider.getHandler(wrapper.entityName(), wrapper.actionName());
        }

        return handler;
    }

    @Override
    public boolean validateRollbackCommand(final CommandWrapper commandWrapper, final AppUser user) {
        user.validateHasPermissionTo(commandWrapper.getTaskPermissionName());
        boolean isMakerChecker = configurationDomainService.isMakerCheckerEnabledForTask(commandWrapper.taskPermissionName());
        return isMakerChecker && !user.isCheckerSuperUser();
    }

    protected void publishHookEvent(final String entityName, final String actionName, JsonCommand command, final Object result) {

        final AppUser appUser = context.authenticatedUser(CommandWrapper.wrap(actionName, entityName, null, null));

        final HookEventSource hookEventSource = new HookEventSource(entityName, actionName);

        // TODO: Add support for publishing array events
        if (command.json() != null) {
            Type type = new TypeToken<Map<String, Object>>() {

            }.getType();

            Map<String, Object> myMap;

            try {
                myMap = gson.fromJson(command.json(), type);
            } catch (Exception e) {
                throw new PlatformApiDataValidationException("error.msg.invalid.json", "The provided JSON is invalid.", new ArrayList<>(),
                        e);
            }

            Map<String, Object> reqmap = new HashMap<>();
            reqmap.put("entityName", entityName);
            reqmap.put("actionName", actionName);
            reqmap.put("createdBy", context.authenticatedUser().getId());
            reqmap.put("createdByName", context.authenticatedUser().getUsername());
            reqmap.put("createdByFullName", context.authenticatedUser().getDisplayName());

            reqmap.put("request", myMap);
            if (result instanceof CommandProcessingResult) {
                CommandProcessingResult resultCopy = CommandProcessingResult.fromCommandProcessingResult((CommandProcessingResult) result);

                reqmap.put("officeId", resultCopy.getOfficeId());
                reqmap.put("clientId", resultCopy.getClientId());
                resultCopy.setOfficeId(null);
                reqmap.put("response", resultCopy);
            } else if (result instanceof ErrorInfo ex) {
                reqmap.put("status", "Exception");

                Map<String, Object> errorMap = new HashMap<>();

                try {
                    errorMap = gson.fromJson(ex.getMessage(), type);
                } catch (Exception e) {
                    errorMap.put("errorMessage", ex.getMessage());
                }

                errorMap.put("errorCode", ex.getErrorCode());
                errorMap.put("statusCode", ex.getStatusCode());

                reqmap.put("response", errorMap);
            }

            reqmap.put("timestamp", Instant.now().toString());

            final String serializedResult = toApiResultJsonSerializer.serialize(reqmap);

            final HookEvent applicationEvent = new HookEvent(hookEventSource, serializedResult, appUser,
                    ThreadLocalContextUtil.getContext());

            applicationContext.publishEvent(applicationEvent);
        }
    }

}
