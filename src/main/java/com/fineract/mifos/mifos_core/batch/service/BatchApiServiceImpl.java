package com.fineract.mifos.mifos_core.batch.service;

import com.fineract.mifos.mifos_core.batch.command.CommandContext;
import com.fineract.mifos.mifos_core.batch.command.CommandStrategy;
import com.fineract.mifos.mifos_core.batch.command.CommandStrategyProvider;
import com.fineract.mifos.mifos_core.batch.dtos.BatchRequest;
import com.fineract.mifos.mifos_core.batch.dtos.BatchResponse;
import com.fineract.mifos.mifos_core.batch.dtos.Header;
import com.fineract.mifos.mifos_core.batch.exception.BatchExecutionException;
import com.fineract.mifos.mifos_core.batch.exception.BatchReferenceInvalidException;
import com.fineract.mifos.mifos_core.batch.exception.ErrorInfo;
import com.fineract.mifos.mifos_core.infrastructure.core.exception.ErrorHandler;
import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPathException;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionExecution;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.ws.rs.core.UriInfo;
import io.github.resilience4j.core.*;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;

/**
 * Implementation for {@link BatchApiService} to iterate through all the incoming requests and obtain the appropriate
 * CommandStrategy from CommandStrategyProvider.
 *
 * @author Peter Gichuki
 * @see com.fineract.mifos.mifos_core.batch.dtos.BatchRequest
 * @see com.fineract.mifos.mifos_core.batch.dtos.BatchResponse
 * @see com.fineract.mifos.mifos_core.batch.command.CommandStrategyProvider
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BatchApiServiceImpl implements BatchApiService {
    private final CommandStrategyProvider strategyProvider;
    private final ResolutionHelper resolutionHelper;
    private final PlatformTransactionManager transactionManager;
    private final ErrorHandler errorHandler;

    private final List<BatchFilter> batchFilters;

    private final List<BatchRequestPreprocessor> batchPreprocessors;

    /**
     * Run each request root step in a separated transaction
     *
     */
    @Override
    public List<BatchResponse> handleBatchRequestsWithoutEnclosingTransaction(final List<BatchRequest> requestList, UriInfo uriInfo) {
        return handleBatchRequests(requestList, uriInfo, false);
    }
    /**
     * Run the batch request in transaction
     *
     * @param requestList
     * @param uriInfo
     * @return
     */

    @Override
    public List<BatchResponse> handleBatchRequestsWithEnclosingTransaction(final List<BatchRequest> requestList, final UriInfo uriInfo) {
        return handleBatchRequests(requestList, uriInfo, true);
    }

    private List<BatchResponse> handleBatchRequests(final List<BatchRequest> requestList, final UriInfo uriInfo,
                                                    boolean enclosingTransaction) {
        BatchRequestContextHolder.setIsEnclosingTransaction(enclosingTransaction);
        try {
            return enclosingTransaction ? callInTransaction(Function.identity()::apply, () -> handleRequestNodes(requestList, uriInfo))
                    : handleRequestNodes(requestList, uriInfo);
        } finally {
            BatchRequestContextHolder.resetIsEnclosingTransaction();
        }
    }

    /**
     * Helper method to run the command in transaction
     *
     * @param request
     *            the enclosing supplier of the command
     * @param transactionConfigurator
     *            consumer to configure the transaction behavior and isolation
     * @return
     */
    private List<BatchResponse> callInTransaction(Consumer<TransactionTemplate> transactionConfigurator,
                                                  Supplier<List<BatchResponse>> request) {
        List<BatchResponse> responseList = new ArrayList<>();
        try {
            TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
            transactionConfigurator.accept(transactionTemplate);
            return transactionTemplate.execute(status -> {
                BatchRequestContextHolder.setEnclosingTransaction(status);
                try {
                    responseList.addAll(request.get());
                    return responseList;
                } catch (BatchExecutionException ex) {
                    log.error("Exception during the batch request processing", ex);
                    responseList.add(buildErrorResponse(ex.getCause(), ex.getRequest()));
                    return responseList;
                } finally {
                    BatchRequestContextHolder.resetTransaction();
                }
            });
        } catch (TransactionException | NonTransientDataAccessException ex) {
            return buildErrorResponses(ex, responseList);
        }
    }
    /**
     * Returns the response list by getting a proper {@link com.fineract.mifos.mifos_core.batch.command.CommandStrategy}.
     * execute() method of acquired commandStrategy is then provided with the separate Request.
     *
     * @return {@code List<BatchResponse>}
     */

    private List<BatchResponse> handleRequestNodes(final List<BatchRequest> requestList, final UriInfo uriInfo) {
        final List<ResolutionHelper.BatchRequestNode> rootNodes;
        try {
            rootNodes = this.resolutionHelper.buildNodesTree(requestList);
        } catch (BatchReferenceInvalidException e) {
            return List.of(buildOrThrowErrorResponse(e, null));
        }

        final ArrayList<BatchResponse> responseList = new ArrayList<>(requestList.size());
        for (ResolutionHelper.BatchRequestNode rootNode : rootNodes) {
            this.callRequestRecursive(rootNode.getRequest(), rootNode, responseList, uriInfo);
        }
        responseList.sort(Comparator.comparing(BatchResponse::getRequestId));
        return responseList;
    }

    /**
     * Executes the request and call child requests recursively.
     *
     * @param request
     *            the current batch request
     * @param requestNode
     *            the batch request holder node
     * @param responseList
     *            the collected responses
     * @return {@code BatchResponse}
     */

    private void callRequestRecursive(BatchRequest request, ResolutionHelper.BatchRequestNode requestNode, List<BatchResponse> responseList,
                                      UriInfo uriInfo) {
        // run current node
        BatchResponse response = executeRequest(request, uriInfo);
        responseList.add(response);
        if (response.getStatusCode() != null && response.getStatusCode() == SC_OK) {
            // run child nodes
            requestNode.getChildNodes().forEach(childNode -> {
                BatchRequest childRequest = childNode.getRequest();
                BatchRequest resolvedChildRequest;
                try {
                    resolvedChildRequest = this.resolutionHelper.resolveRequest(childRequest, response);
                    callRequestRecursive(resolvedChildRequest, childNode, responseList, uriInfo);
                } catch (JsonPathException jpex) {
                    responseList.add(buildOrThrowErrorResponse(jpex, childRequest));
                }
            });
        } else {
            responseList.addAll(parentRequestFailedRecursive(request, requestNode, response, null));
        }
        // If the current request fails, then all the child requests are not executed. If we want to write out all the
        // child requests, here is the place.
    }
    /**
     * Execute the request
     *
     * @param request
     * @param uriInfo
     * @return
     */
    private BatchResponse executeRequest(BatchRequest request, UriInfo uriInfo) {
        final CommandStrategy commandStrategy = this.strategyProvider
                .getCommandStrategy(CommandContext.resource(request.getRelativeUrl()).method(request.getMethod()).build());
        log.debug("Batch request: method [{}], relative url [{}]", request.getMethod(), request.getRelativeUrl());
        Either<RuntimeException, BatchRequest> preprocessorResult = runPreprocessors(request);
        if (preprocessorResult.isLeft()) {
            return buildOrThrowErrorResponse(preprocessorResult.getLeft(), request);
        } else {
            request = preprocessorResult.get();
        }
        try {
            BatchRequestContextHolder.setRequestAttributes(new HashMap<>(Optional.ofNullable(request.getHeaders())
                    .map(list -> list.stream().collect(Collectors.toMap(Header::getName, Header::getValue)))
                    .orElse(Collections.emptyMap())));
            if (BatchRequestContextHolder.isEnclosingTransaction()) {
                entityManager.flush();
            }
            BatchCallHandler callHandler = new BatchCallHandler(this.batchFilters, commandStrategy::execute);
            final BatchResponse rootResponse = callHandler.serviceCall(request, uriInfo);
            log.debug("Batch response: status code [{}], method [{}], relative url [{}]", rootResponse.getStatusCode(), request.getMethod(),
                    request.getRelativeUrl());
            return rootResponse;
        } catch (RuntimeException ex) {
            return buildOrThrowErrorResponse(ex, request);
        } finally {
            BatchRequestContextHolder.resetRequestAttributes();
        }
    }

    private Either<RuntimeException, BatchRequest> runPreprocessors(BatchRequest request) {
        return runPreprocessor(batchPreprocessors, request);
    }

    private Either<RuntimeException, BatchRequest> runPreprocessor(List<BatchRequestPreprocessor> remainingPreprocessor,
                                                                   BatchRequest request) {
        if (remainingPreprocessor.isEmpty()) {
            return Either.right(request);
        } else {
            BatchRequestPreprocessor preprocessor = remainingPreprocessor.get(0);
            Either<RuntimeException, BatchRequest> processingResult = preprocessor.preprocess(request);
            if (processingResult.isLeft()) {
                return processingResult;
            } else {
                return runPreprocessor(remainingPreprocessor.subList(1, remainingPreprocessor.size()), processingResult.get());
            }
        }
    }

    /**
     * All requests recursively are set to status 409 if the parent request fails.
     *
     * @param request
     *            the current request
     * @param requestNode
     *            the current request node
     * @return {@code BatchResponse} list of the generated batch responses
     */
    private List<BatchResponse> parentRequestFailedRecursive(@NotNull BatchRequest request, @NotNull ResolutionHelper.BatchRequestNode requestNode,
                                                             @NotNull BatchResponse response, Long parentId) {
        List<BatchResponse> responseList = new ArrayList<>();
        if (parentId == null) { // root
            BatchRequestContextHolder.getEnclosingTransaction().ifPresent(TransactionExecution::setRollbackOnly);
        } else {
            responseList.add(buildErrorResponse(request.getRequestId(), response.getStatusCode(),
                    "Parent request with id " + parentId + " was erroneous!", null));
        }
        requestNode.getChildNodes().forEach(childNode -> responseList
                .addAll(parentRequestFailedRecursive(childNode.getRequest(), childNode, response, request.getRequestId())));
        return responseList;
    }

    /**
     * Return the response when any exception raised
     *
     * @param ex
     *            the exception
     * @param request
     *            the called request
     */
    private BatchResponse buildErrorResponse(Throwable ex, BatchRequest request) {
        Long requestId = null;
        Integer statusCode = null;
        String body = null;
        Set<Header> headers = new HashSet<>();
        if (ex != null) {
            ErrorInfo errorInfo = errorHandler.handle(ErrorHandler.getMappable(ex));
            statusCode = errorInfo.getStatusCode();
            body = errorInfo.getMessage();
            headers = Optional.ofNullable(errorInfo.getHeaders()).orElse(new HashSet<>());
        }
        if (request != null) {
            requestId = request.getRequestId();
            if (request.getHeaders() != null) {
                headers.addAll(request.getHeaders());
            }
        }
        return buildErrorResponse(requestId, statusCode, body, headers);
    }

    private BatchResponse buildOrThrowErrorResponse(RuntimeException ex, BatchRequest request) {
        BatchResponse response = buildErrorResponse(ex, request);
        if (response.getStatusCode() != SC_OK && BatchRequestContextHolder.isEnclosingTransaction()) {
            BatchRequestContextHolder.getTransaction().ifPresent(TransactionExecution::setRollbackOnly);
            throw new BatchExecutionException(request, ex);
        }
        return response;
    }

    @NotNull
    private List<BatchResponse> buildErrorResponses(Throwable ex, @NotNull List<BatchResponse> responseList) {
        BatchResponse response = responseList.isEmpty() ? null
                : responseList.stream().filter(e -> e.getStatusCode() == null || e.getStatusCode() != SC_OK).findFirst()
                .orElse(responseList.get(responseList.size() - 1));

        if (response != null && response.getStatusCode() == SC_OK && ex instanceof TransactionSystemException tse) {
            ex = new ConcurrencyFailureException(tse.getMessage(), tse.getCause());
        }

        Long requestId = null;
        Integer statusCode = null;
        String body = null;
        Set<Header> headers = new HashSet<>();
        if (ex != null) {
            ErrorInfo errorInfo = errorHandler.handle(ErrorHandler.getMappable(ex));
            statusCode = errorInfo.getStatusCode();
            body = errorInfo.getMessage();
            headers = Optional.ofNullable(errorInfo.getHeaders()).orElse(new HashSet<>());
        }
        if (response != null) {
            requestId = response.getRequestId();
            Integer responseCode = response.getStatusCode();
            if (responseCode == null || responseCode != SC_OK) {
                if (responseCode != null) {
                    statusCode = responseCode;
                }
                body = "Transaction is being rolled back. First erroneous request: \n" + new Gson().toJson(response);
            }
            if (response.getHeaders() != null) {
                headers.addAll(response.getHeaders());
            }
        }
        return List.of(buildErrorResponse(requestId, statusCode, body, headers));
    }

    @SuppressFBWarnings(value = "BX_UNBOXING_IMMEDIATELY_REBOXED", justification = "TODO: fix this!")
    private BatchResponse buildErrorResponse(Long requestId, Integer statusCode, String body, Set<Header> headers) {
        return new BatchResponse().setRequestId(requestId).setStatusCode(statusCode == null ? SC_INTERNAL_SERVER_ERROR : statusCode)
                .setBody(body == null ? "Request with id " + requestId + " was erroneous!" : body).setHeaders(headers);
    }

}
