package com.fineract.mifos.mifos_core.infrastructure.core.api;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import java.util.Set;

/**
FIXME - KW - General thing to fix about REST API is if use partial response approach of fields=id,name,selectedPermissions
      It will return just id,name parameters of RoleData and ignore description, however as PermissionData used in selectedPermissions collection
     also has a field called description it gets ignored also. This is because of the implementation of ParameterListExclusionStrategy which doesnt take
       into account the Object its looking at.
 */

public class ParameterListExclusionStrategy implements ExclusionStrategy {

    private final Set<String> parameterNamesToSkip;

    public ParameterListExclusionStrategy(final Set<String> parameterNamesToSkip) {
        this.parameterNamesToSkip = parameterNamesToSkip;
    }

    @Override
    public boolean shouldSkipField(final FieldAttributes f) {
        return this.parameterNamesToSkip.contains(f.getName());
    }

    @SuppressWarnings("unused")
    @Override
    public boolean shouldSkipClass(final Class<?> clazz) {
        return false;
    }
}
