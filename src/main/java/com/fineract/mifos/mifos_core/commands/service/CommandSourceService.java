package com.fineract.mifos.mifos_core.commands.service;

import com.fineract.mifos.mifos_core.batch.exception.ErrorInfo;
import com.fineract.mifos.mifos_core.commands.dtos.CommandWrapper;
import com.fineract.mifos.mifos_core.commands.entity.CommandSource;
import com.fineract.mifos.mifos_core.commands.exception.CommandNotFoundException;
import com.fineract.mifos.mifos_core.commands.exception.RollbackTransactionNotApprovedException;
import com.fineract.mifos.mifos_core.commands.handler.NewCommandSourceHandler;
import com.fineract.mifos.mifos_core.commands.repository.CommandSourceRepository;
import com.fineract.mifos.mifos_core.infrastructure.core.api.JsonCommand;
import com.fineract.mifos.mifos_core.infrastructure.core.data.CommandProcessingResult;
import com.fineract.mifos.mifos_core.infrastructure.core.exception.ErrorHandler;
import com.fineract.mifos.mifos_core.useradministration.entity.AppUser;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.fineract.mifos.mifos_core.commands.dtos.CommandProcessingResultType.UNDER_PROCESSING;

/**
 * Two phase transactional command processing: save initial...work...finish/failed to handle idempotent requests. As the
 * default isolation level for MYSQL is REPEATABLE_READ and a lower value READ_COMMITED for postgres, we can force to
 * use the same for both database backends to be consistent.
 */
@Component
@RequiredArgsConstructor
public class CommandSourceService {
    private final CommandSourceRepository commandSourceRepository;
    private final ErrorHandler errorHandler;

    @NotNull
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
    public CommandSource saveInitialNewTransaction(CommandWrapper wrapper, JsonCommand jsonCommand, AppUser maker, String idempotencyKey) {
        return saveInitial(wrapper, jsonCommand, maker, idempotencyKey);
    }

    @NotNull
    @Transactional(propagation = Propagation.REQUIRED)
    public CommandSource saveInitialSameTransaction(CommandWrapper wrapper, JsonCommand jsonCommand, AppUser maker, String idempotencyKey) {
        return saveInitial(wrapper, jsonCommand, maker, idempotencyKey);
    }

    @NotNull
    private CommandSource saveInitial(CommandWrapper wrapper, JsonCommand jsonCommand, AppUser maker, String idempotencyKey) {
        CommandSource initialCommandSource = getInitialCommandSource(wrapper, jsonCommand, maker, idempotencyKey);
        return commandSourceRepository.saveAndFlush(initialCommandSource);
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
    public CommandSource saveResultNewTransaction(@NotNull CommandSource commandSource) {
        return saveResult(commandSource);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CommandSource saveResultSameTransaction(@NotNull CommandSource commandSource) {
        return saveResult(commandSource);
    }

    @NotNull
    private CommandSource saveResult(@NotNull CommandSource commandSource) {
        return commandSourceRepository.saveAndFlush(commandSource);
    }

    public ErrorInfo generateErrorInfo(Throwable t) {
        return errorHandler.handle(ErrorHandler.getMappable(t));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public CommandSource getCommandSource(Long commandSourceId) {
        return commandSourceRepository.findById(commandSourceId).orElseThrow(() -> new CommandNotFoundException(commandSourceId));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CommandSource findCommandSource(CommandWrapper wrapper, String idempotencyKey) {
        return commandSourceRepository.findByActionNameAndEntityNameAndIdempotencyKey(wrapper.actionName(), wrapper.entityName(),
                idempotencyKey);
    }

    public CommandSource getInitialCommandSource(CommandWrapper wrapper, JsonCommand jsonCommand, AppUser maker, String idempotencyKey) {
        CommandSource commandSourceResult = CommandSource.fullEntryFrom(wrapper, jsonCommand, maker, idempotencyKey,
                UNDER_PROCESSING.getValue());
        if (commandSourceResult.getCommandJson() == null) {
            commandSourceResult.setCommandJson("{}");
        }
        return commandSourceResult;
    }

    @Transactional
    public CommandProcessingResult processCommand(NewCommandSourceHandler handler, JsonCommand command, CommandSource commandSource,
                                                  AppUser user, boolean isApprovedByChecker, boolean isMakerChecker) {
        final CommandProcessingResult result = handler.processCommand(command);
        boolean isRollback = !isApprovedByChecker && !user.isCheckerSuperUser() && (isMakerChecker || result.isRollbackTransaction());
        if (isRollback) {
            commandSource.markAsAwaitingApproval();
            throw new RollbackTransactionNotApprovedException(commandSource.getId(), commandSource.getResourceId());
        }
        return result;
    }

}
