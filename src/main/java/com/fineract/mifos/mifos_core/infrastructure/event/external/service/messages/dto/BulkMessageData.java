package com.fineract.mifos.mifos_core.infrastructure.event.external.service.messages.dto;

import lombok.Getter;

import java.util.Objects;

@Getter
public class BulkMessageData {

    private final BulkMessagePayloadV1 data;

    public BulkMessageData(BulkMessagePayloadV1 data) {
        this.data = Objects.requireNonNull(data, "data cannot be null");
    }
}
