package com.fineract.mifos.mifos_core.infrastructure.core.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;

/**
 * A Google GSON implementation of contract.
 *
 * It serializes all fields of any Java {@link Object} passed to it.
 */
@Component
public final class ExcludeNothingWithPrettyPrintingOnJsonSerializerGoogleGson {

    private final Gson gson;

    public ExcludeNothingWithPrettyPrintingOnJsonSerializerGoogleGson() {
        final GsonBuilder builder = new GsonBuilder();
        GoogleGsonSerializerHelper.registerTypeAdapters(builder);
        builder.setPrettyPrinting();

        this.gson = builder.create();
    }

    public String serialize(final Object result) {
        return this.gson.toJson(result);
    }
}

