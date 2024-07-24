package com.fineract.mifos.mifos_core.infrastructure.event.external.service.idempotency;

import com.fineract.mifos.mifos_core.infrastructure.event.business.dto.BusinessEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DefaultExternalEventIdempotencyKeyGenerator implements ExternalEventIdempotencyKeyGenerator {

    @Override
    public <T> String generate(BusinessEvent<T> event) {
        return UUID.randomUUID().toString();
    }
}
