package com.fineract.mifos.core.infrastructure.core.api;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import java.util.Set;

public class ParameterListInclusionStrategy implements ExclusionStrategy {
    private final Set<String> parameterNamesToInclude;

    public ParameterListInclusionStrategy(final Set<String> parameterNamesToSkip) {
        this.parameterNamesToInclude = parameterNamesToSkip;
    }

    @Override
    public boolean shouldSkipField(final FieldAttributes f) {
        return !this.parameterNamesToInclude.contains(f.getName());
    }

    @Override
    public boolean shouldSkipClass(final Class<?> clazz) {
        return false;
    }
}
