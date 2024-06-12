package com.fineract.mifos.mifos_core.batch.controller;

import com.fineract.mifos.mifos_core.batch.dtos.BatchRequest;
import com.fineract.mifos.mifos_core.batch.dtos.BatchResponse;
import com.fineract.mifos.mifos_core.batch.serialization.BatchRequestJsonHelper;
import com.fineract.mifos.mifos_core.batch.service.BatchApiService;
import com.fineract.mifos.mifos_core.infrastructure.core.serialization.ToApiJsonSerializer;
import com.fineract.mifos.mifos_core.infrastructure.security.exception.InvalidInstanceTypeMethodException;
import com.fineract.mifos.mifos_core.infrastructure.security.service.PlatformSecurityContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.Optional;

/**
 * Provides a REST controller for Batch Requests. This class acts as a proxy to
 * {@link com.fineract.mifos.mifos_core.batch.service.BatchApiService} and de-serializes the incoming JSON string to a list of
 * {@link com.fineract.mifos.mifos_core.batch.dtos.BatchRequest} type. This list is forwarded to BatchApiService which finally
 * returns a list of {@link com.fineract.mifos.mifos_core.batch.dtos.BatchResponse} type which is then serialized into JSON
 * response by this Resource class.
 *
 * @author Peter Gichuki
 *
 * @see com.fineract.mifos.mifos_core.batch.service.BatchApiService
 * @see com.fineract.mifos.mifos_core.batch.dtos.BatchRequest
 * @see com.fineract.mifos.mifos_core.batch.dtos.BatchResponse
 */

@RestController
@RequestMapping("/v1/batches")
@Tag(name = "Batch API", description = "The Apache Fineract Batch API enables a consumer to access significant amounts of data in a single call or to make changes to several objects at once. Batching allows a consumer to pass instructions for several operations in a single HTTP request. A consumer can also specify dependencies between related operations. Once all operations have been completed, a consolidated response will be passed back and the HTTP connection will be closed.\n"
        + "\n"
        + "The Batch API takes in an array of logical HTTP requests represented as JSON arrays - each request has a requestId (the id of a request used to specify the sequence and as a dependency between requests), a method (corresponding to HTTP method GET/PUT/POST/DELETE etc.), a relativeUrl (the portion of the URL after https://example.org/api/v2/), optional headers array (corresponding to HTTP headers), optional reference parameter if a request is dependent on another request and an optional body (for POST and PUT requests). The Batch API returns an array of logical HTTP responses represented as JSON arrays - each response has a requestId, a status code, an optional headers array and an optional body (which is a JSON encoded string).\n"
        + "\n"
        + "Batch API uses Json Path to handle dependent parameters. For example, if request '2' is referencing request '1' and in the \"body\" or in \"relativeUrl\" of request '2', there is a dependent parameter (which will look like \"$.parameter_name\"), then Batch API will internally substitute this dependent parameter from the response body of request '1'.\n"
        + "\n"
        + "Batch API is able to handle deeply nested dependent requests as well nested parameters. As shown in the example, requests are dependent on each other as, 1<--2<--6, i.e a nested dependency, where request '6' is not directly dependent on request '1' but still it is one of the nested child of request '1'. In the same way Batch API could handle a deeply nested dependent value, such as {..[..{..,$.parameter_name,..}..]}.")
@RequiredArgsConstructor
public class BatchApiResourceController {
    private final PlatformSecurityContext context;
    private final ToApiJsonSerializer<BatchResponse> toApiJsonSerializer;
    private final BatchApiService service;
    private final BatchRequestJsonHelper batchRequestJsonHelper;
    private final FineractProperties fineractProperties;

    /**
     * Rest assured POST method to get {@link BatchRequest} and returns back the consolidated {@link BatchResponse}
     *
     * @return serialized JSON
     */
    @PostMapping
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    @Operation(summary = "Batch requests in a single transaction", description = "The Apache Fineract Batch API is also capable of executing all the requests in a single transaction, by setting a Query Parameter, \"enclosingTransaction=true\". So, if one or more of the requests in a batch returns an erroneous response all of the Data base transactions made by other successful requests will be rolled back.\n"
            + "\n"
            + "If there has been a rollback in a transaction then a single response will be provided, with a '400' status code and a body consisting of the error details of the first failed request.")
    @RequestBody(required = true, content = @Content(array = @ArraySchema(schema = @Schema(implementation = BatchRequest.class, description = "request body"))))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(array = @ArraySchema(schema = @Schema(implementation = BatchResponse.class)))) })
    public String handleBatchRequests(
            @DefaultValue("false") @QueryParam("enclosingTransaction") @Parameter(description = "enclosingTransaction", required = false) final boolean enclosingTransaction,
            @Parameter(hidden = true) final String jsonRequestString, @Context UriInfo uriInfo) {

        // Handles user authentication
        this.context.authenticatedUser();

        // Converts request array into BatchRequest List
        final List<BatchRequest> requestList = this.batchRequestJsonHelper.extractList(jsonRequestString);

        validateRequestMethodsAllowedOnInstanceType(requestList);

        // Gets back the consolidated BatchResponse from BatchApiservice
        List<BatchResponse> result;

        // If the request is to be handled as a Transaction. All requests will
        // be rolled back on error
        if (enclosingTransaction) {
            result = service.handleBatchRequestsWithEnclosingTransaction(requestList, uriInfo);
        } else {
            result = service.handleBatchRequestsWithoutEnclosingTransaction(requestList, uriInfo);
        }

        return this.toApiJsonSerializer.serialize(result);

    }

    /**
     * Validates to make sure the request methods are allowed on currently running instance mode (type).
     *
     * @param requestList
     *            the list of {@link BatchRequest}s
     */
    private void validateRequestMethodsAllowedOnInstanceType(final List<BatchRequest> requestList) {
        // Throw exception if instance is read only and any of the batch requests are trying to write/update data.
        if (fineractProperties.getMode().isReadOnlyMode()) {
            final Optional<BatchRequest> nonGetRequest = requestList.stream()
                    .filter(batchRequest -> !HttpMethod.GET.equals(batchRequest.getMethod())).findFirst();
            if (nonGetRequest.isPresent()) {
                throw new InvalidInstanceTypeMethodException(nonGetRequest.get().getMethod());
            }
        }
    }


}
