package com.fineract.mifos.core.infrastructure.core.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Serializer for Java Offset Date Time {@link OffsetDateTime} that returns the date in ISO-8601 Offset date time format
 */
public class OffsetDateTimeAdapter implements JsonSerializer<OffsetDateTime> {

    @Override
    @SuppressWarnings("unused")
    public JsonElement serialize(final OffsetDateTime dateTime, final Type typeOfSrc, final JsonSerializationContext context) {
        JsonElement object = null;
        if (dateTime != null) {
            object = new JsonPrimitive(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(dateTime));
        }
        return object;
    }
}
