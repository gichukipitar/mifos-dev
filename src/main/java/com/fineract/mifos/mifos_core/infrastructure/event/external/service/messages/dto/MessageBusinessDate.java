package com.fineract.mifos.mifos_core.infrastructure.event.external.service.messages.dto;

import lombok.Getter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
public class MessageBusinessDate {

    private LocalDate businessDate;

    public MessageBusinessDate(LocalDate businessDate) {
        this.businessDate = Objects.requireNonNull(businessDate, "businessDate cannot be null");
    }
}
