package com.fineract.mifos.core.infrastructure.core.api;

import com.fineract.mifos.core.infrastructure.core.domain.ExternalId;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class ExternalIdAdapter implements JsonSerializer<ExternalId> {

    @Override
    @SuppressWarnings("unused")
    public JsonElement serialize(ExternalId src, Type typeOfSrc, JsonSerializationContext context) {
        if (src == null || src.isEmpty()) {
            return null;
        }
        return new JsonPrimitive(src.getValue());
    }
}
