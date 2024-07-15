package com.fineract.mifos.mifos_core.infrastructure.core.service;

public interface DataEnricher<T> {

    boolean isDataTypeSupported(Class<T> dataType);

    void enrich(T data);
}
