package com.fineract.mifos.mifos_core.infrastructure.core.filters;

import com.fineract.mifos.mifos_core.batch.dtos.BatchRequest;
import com.fineract.mifos.mifos_core.batch.dtos.BatchResponse;

import javax.ws.rs.core.UriInfo;

public interface BatchFilterChain {
    BatchResponse serviceCall(BatchRequest batchRequest, UriInfo uriInfo);
}