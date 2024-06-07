package com.fineract.mifos.core.infrastructure.core.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateAdapter implements JsonSerializer<Date> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
    @Override
    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        if (src == null) {
            return null;
        }
        return new JsonPrimitive(formatter.format(src.toInstant()));
    }
}
