package com.fineract.mifos.mifos_core.infrastructure.core.filters;

import com.fineract.mifos.mifos_core.batch.dtos.BatchRequest;
import com.fineract.mifos.mifos_core.batch.dtos.BatchResponse;
import lombok.RequiredArgsConstructor;

import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.function.BiFunction;

@RequiredArgsConstructor
public class BatchCallHandler implements BatchFilterChain{
    private final List<? extends BatchFilter> filters;

    private final BiFunction<BatchRequest, UriInfo, BatchResponse> lastElement;
    private int currentPosition = 0;

    @Override
    public BatchResponse serviceCall(BatchRequest batchRequest, UriInfo uriInfo) {
        if (this.currentPosition == filters.size()) {
            return lastElement.apply(batchRequest, uriInfo);
        } else {
            BatchFilter currentFilter = filters.get(this.currentPosition);
            this.currentPosition++;
            return currentFilter.doFilter(batchRequest, uriInfo, this);
        }
    }
}
