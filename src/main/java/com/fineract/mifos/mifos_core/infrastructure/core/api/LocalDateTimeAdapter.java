package com.fineract.mifos.mifos_core.infrastructure.core.api;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;

/**
 * Serializer for Java Local Date Time {@link LocalDateTime} that returns the date in array format to match previous
 * Jackson functionality.
 */
public class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime> {

    @Override
    @SuppressWarnings("unused")
    public JsonElement serialize(final LocalDateTime src, final Type typeOfSrc, final JsonSerializationContext context) {
        JsonArray array = null;
        if (src != null) {
            array = new JsonArray();
            array.add(new JsonPrimitive(src.get(ChronoField.YEAR_OF_ERA)));
            array.add(new JsonPrimitive(src.getMonthValue()));
            array.add(new JsonPrimitive(src.getDayOfMonth()));
            array.add(new JsonPrimitive(src.getHour()));
            array.add(new JsonPrimitive(src.getMinute()));
            array.add(new JsonPrimitive(src.getSecond()));
        }
        return array;
    }
}
