package com.fineract.mifos.mifos_core.infrastructure.event.external.service.messages.dto;

import lombok.Getter;

import java.util.Objects;

@Getter
public class MessageIdempotencyKey {

    private final String idempotencyKey;

    public MessageIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = Objects.requireNonNull(idempotencyKey, "idempotencyKey cannot be null");
    }
}
