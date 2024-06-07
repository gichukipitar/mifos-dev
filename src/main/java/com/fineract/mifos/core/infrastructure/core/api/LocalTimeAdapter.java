package com.fineract.mifos.core.infrastructure.core.api;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalTime;

/**
 * Serializer for Java Local Time {@link LocalTime} that returns the time in array format to match previous Jackson
 * functionality.
 */
public class LocalTimeAdapter implements JsonSerializer<LocalTime> {

    @Override
    @SuppressWarnings("unused")
    public JsonElement serialize(final LocalTime src, final Type typeOfSrc, final JsonSerializationContext context) {
        JsonArray array = null;
        if (src != null) {
            array = new JsonArray();
            array.add(new JsonPrimitive(src.getHour()));
            array.add(new JsonPrimitive(src.getMinute()));
            array.add(new JsonPrimitive(src.getSecond()));
        }
        return array;
    }
}

