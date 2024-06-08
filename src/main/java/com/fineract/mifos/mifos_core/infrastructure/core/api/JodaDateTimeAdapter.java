package com.fineract.mifos.mifos_core.infrastructure.core.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.ZonedDateTime;

/**
 * Serializer for Joda Time {@link ZonedDateTime} that returns the date as long to match previous (Jackson)
 * functionality.
 */
public class JodaDateTimeAdapter implements JsonSerializer<ZonedDateTime> {

    @Override
    @SuppressWarnings("unused")
    public JsonElement serialize(final ZonedDateTime src, final Type typeOfSrc, final JsonSerializationContext context) {
        JsonElement element = null;
        if (src != null) {
            element = new JsonPrimitive(src.toInstant().toEpochMilli());
        }

        return element;
    }
}
