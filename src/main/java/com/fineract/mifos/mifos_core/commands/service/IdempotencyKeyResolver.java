package com.fineract.mifos.mifos_core.commands.service;

import com.fineract.mifos.mifos_core.commands.dtos.CommandWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class IdempotencyKeyResolver {
    private final FineractRequestContextHolder fineractRequestContextHolder;

    private final IdempotencyKeyGenerator idempotencyKeyGenerator;

    public String resolve(CommandWrapper wrapper) {
        return Optional.ofNullable(wrapper.getIdempotencyKey()).orElseGet(() -> getAttribute().orElseGet(idempotencyKeyGenerator::create));
    }

    private Optional<String> getAttribute() {
        return Optional.ofNullable(fineractRequestContextHolder.getAttribute(SynchronousCommandProcessingService.IDEMPOTENCY_KEY_ATTRIBUTE))
                .map(String::valueOf);

    }
}
