package com.fineract.mifos.core.infrastructure.core.api;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.temporal.ChronoField;

public class LocalDateAdapter implements JsonSerializer<LocalDate> {

    @Override
    @SuppressWarnings("unused")
    public JsonElement serialize(final LocalDate src, final Type typeOfSrc, final JsonSerializationContext context) {
        JsonArray array = null;
        if (src != null) {
            array = new JsonArray();
            array.add(new JsonPrimitive(src.get(ChronoField.YEAR_OF_ERA)));
            array.add(new JsonPrimitive(src.getMonthValue()));
            array.add(new JsonPrimitive(src.getDayOfMonth()));
        }
        return array;
    }
}