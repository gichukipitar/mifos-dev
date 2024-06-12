package com.fineract.mifos.mifos_core.batch.service;

import com.fineract.mifos.mifos_core.batch.dtos.BatchRequest;
import com.fineract.mifos.mifos_core.batch.dtos.BatchResponse;

import javax.ws.rs.core.UriInfo;
import java.util.List;

/**
 * Provides an interface for service class, that implements the method to handle separate Batch Requests.
 *
 * @author Peter Gichuki
 *
 * @see com.fineract.mifos.mifos_core.batch.dtos.BatchRequest
 * @see com.fineract.mifos.mifos_core.batch.dtos.BatchResponse
 * @see BatchApiServiceImpl
 */
public interface BatchApiService {
    /**
     * Returns a list of {@link com.fineract.mifos.mifos_core.batch.dtos.BatchResponse}s by getting the appropriate
     * CommandStrategy for every {@link com.fineract.mifos.mifos_core.batch.dtos.BatchRequest}. It will be used when the Query
     * Parameter "enclosingTransaction "is set to 'false'.
     *
     * @param requestList
     * @param uriInfo
     * @return List&lt;BatchResponse&gt;
     */

    List<BatchResponse> handleBatchRequestsWithoutEnclosingTransaction(List<BatchRequest> requestList, UriInfo uriInfo);

    /**
     * returns a list of {@link com.fineract.mifos.mifos_core.batch.dtos.BatchResponse}s by getting the appropriate
     * CommandStrategy for every {@link com.fineract.mifos.mifos_core.batch.dtos.BatchRequest}. It will be used when the Query
     * Parameter "enclosingTransaction "is set to 'true'. If one or more of the requests are not completed properly then
     * whole of the transaction will be rolled back properly.
     *
     * @param requestList
     * @param uriInfo
     * @return List&lt;BatchResponse&gt;
     */
    List<BatchResponse> handleBatchRequestsWithEnclosingTransaction(List<BatchRequest> requestList, UriInfo uriInfo);

}
