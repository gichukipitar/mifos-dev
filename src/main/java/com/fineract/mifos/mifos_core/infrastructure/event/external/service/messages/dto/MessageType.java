package com.fineract.mifos.mifos_core.infrastructure.event.external.service.messages.dto;

import lombok.Getter;

import java.util.Objects;

@Getter
public class MessageType {

    private final String type;

    public MessageType(String type) {
        this.type = Objects.requireNonNull(type, "type cannot be null");
    }
}