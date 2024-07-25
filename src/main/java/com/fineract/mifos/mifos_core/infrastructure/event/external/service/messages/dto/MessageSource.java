package com.fineract.mifos.mifos_core.infrastructure.event.external.service.messages.dto;

import lombok.Getter;

import java.util.Objects;

@Getter
public class MessageSource {

    private final String source;

    public MessageSource(String source) {
        this.source = Objects.requireNonNull(source, "source cannot be null");
    }
}
