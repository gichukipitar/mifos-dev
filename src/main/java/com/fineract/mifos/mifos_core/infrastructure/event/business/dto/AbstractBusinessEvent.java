package com.fineract.mifos.mifos_core.infrastructure.event.business.dto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractBusinessEvent<T> implements BusinessEvent<T> {

    private final T value;

    @Override
    public T get() {
        return value;
    }
}
