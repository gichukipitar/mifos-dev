package com.fineract.mifos.mifos_core.infrastructure.event.business.dto;

public interface BusinessEvent<T> {

    T get();

    String getType();

    String getCategory();

    Long getAggregateRootId();
}
