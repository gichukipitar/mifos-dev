package com.fineract.mifos.mifos_core.infrastructure.core.filters;

import com.fineract.mifos.mifos_core.batch.dtos.BatchRequest;
import com.fineract.mifos.mifos_core.batch.dtos.BatchResponse;
import com.fineract.mifos.mifos_core.commands.service.SynchronousCommandProcessingService;
import com.fineract.mifos.mifos_core.infrastructure.core.config.FineractProperties;
import com.fineract.mifos.mifos_core.infrastructure.core.dto.FineractRequestContextHolder;
import javax.ws.rs.core.UriInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.fineract.mifos.mifos_core.batch.dtos.Header;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class IdempotencyStoreBatchFilter implements BatchFilter {

    private final FineractRequestContextHolder fineractRequestContextHolder;
    private final IdempotencyStoreHelper helper;
    private final FineractProperties fineractProperties;

    @Override
    public BatchResponse doFilter(BatchRequest batchRequest, UriInfo uriInfo, BatchFilterChain chain) {
        extractIdempotentKeyFromBatchRequest(batchRequest).ifPresent(idempotentKey -> fineractRequestContextHolder
                .setAttribute(SynchronousCommandProcessingService.IDEMPOTENCY_KEY_ATTRIBUTE, idempotentKey));
        BatchResponse result = chain.serviceCall(batchRequest, uriInfo);
        Optional<Long> commandId = helper.getCommandId(null);
        boolean isSuccessWithoutStored = commandId.isPresent() && helper.isStoreIdempotencyKey(null);
        if (isSuccessWithoutStored) {
            helper.storeCommandResult(result.getStatusCode(), result.getBody(), commandId.get());
        }
        return result;
    }

    private Optional<String> extractIdempotentKeyFromBatchRequest(BatchRequest request) {
        if (request.getHeaders() == null) {
            return Optional.empty();
        }
        return request.getHeaders() //
                .stream().filter(header -> header.getName().equals(fineractProperties.getIdempotencyKeyHeaderName())) //
                .map(Header::getValue) //
                .findAny(); //

    }
}
