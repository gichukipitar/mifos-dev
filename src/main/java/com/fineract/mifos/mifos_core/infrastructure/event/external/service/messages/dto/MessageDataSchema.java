package com.fineract.mifos.mifos_core.infrastructure.event.external.service.messages.dto;

import lombok.Getter;

import java.util.Objects;

@Getter
public class MessageDataSchema {

    private final String dataSchema;

    public MessageDataSchema(String dataSchema) {
        this.dataSchema = Objects.requireNonNull(dataSchema, "dataSchema cannot be null");
    }
}
