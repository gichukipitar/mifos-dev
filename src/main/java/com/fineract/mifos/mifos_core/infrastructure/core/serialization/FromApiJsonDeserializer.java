package com.fineract.mifos.mifos_core.infrastructure.core.serialization;

public interface FromApiJsonDeserializer<T> {

    T commandFromApiJson(String json);
}
