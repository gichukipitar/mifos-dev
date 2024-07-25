package com.fineract.mifos.mifos_core.infrastructure.event.external.service.messages.dto;

import lombok.Getter;

import java.nio.ByteBuffer;
import java.util.Objects;

@Getter
public class MessageData {

    private final ByteBuffer data;

    public MessageData(ByteBuffer data) {
        this.data = Objects.requireNonNull(data, "data cannot be null");
    }
}
