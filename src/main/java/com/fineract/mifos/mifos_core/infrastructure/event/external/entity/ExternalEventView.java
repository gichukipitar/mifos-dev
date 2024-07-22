package com.fineract.mifos.mifos_core.infrastructure.event.external.entity;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public interface ExternalEventView {

    Long getId();

    String getType();

    String getCategory();

    String getSchema();

    byte[] getData();

    OffsetDateTime getCreatedAt();

    ExternalEventStatus getStatus();

    OffsetDateTime getSentAt();

    String getIdempotencyKey();

    LocalDate getBusinessDate();

    Long getAggregateRootId();
}
