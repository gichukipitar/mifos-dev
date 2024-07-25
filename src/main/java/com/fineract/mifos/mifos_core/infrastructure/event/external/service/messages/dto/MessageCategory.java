package com.fineract.mifos.mifos_core.infrastructure.event.external.service.messages.dto;

import lombok.Getter;

import java.util.Objects;

@Getter
public class MessageCategory {

    private final String category;

    public MessageCategory(String category) {
        this.category = Objects.requireNonNull(category, "category cannot be null");
    }
}
