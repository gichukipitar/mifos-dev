package com.fineract.mifos.mifos_core.batch.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
