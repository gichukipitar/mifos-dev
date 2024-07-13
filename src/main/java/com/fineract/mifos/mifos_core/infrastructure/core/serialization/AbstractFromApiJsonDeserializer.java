package com.fineract.mifos.mifos_core.infrastructure.core.serialization;

/**
 * Abstract implementation of {@link FromApiJsonDeserializer} that can be extended for specific commands.
 */
public abstract class AbstractFromApiJsonDeserializer<T> implements FromApiJsonDeserializer<T> {

    @Override
    public abstract T commandFromApiJson(String json);
}

