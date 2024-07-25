package com.fineract.mifos.mifos_core.infrastructure.event.external.service.messages.dto;

import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.Objects;

@Getter
public class MessageCreatedAt {

    private OffsetDateTime createdAt;

    public MessageCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt cannot be null");
    }
}
