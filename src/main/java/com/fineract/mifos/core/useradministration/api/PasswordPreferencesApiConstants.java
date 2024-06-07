package com.fineract.mifos.core.useradministration.api;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class PasswordPreferencesApiConstants {

    private PasswordPreferencesApiConstants() {

    }

    public static final String RESOURCE_NAME = "passwordpreferences";
    public static final String ENTITY_NAME = "PASSWORD_PREFERENCES";

    // response parameters
    public static final String DESCRIPTION = "description";

    public static final String ACTIVE = "active";

    public static final String ID_PARAM_NAME = "id";

    // request parameters
    public static final String VALIDATION_POLICY_ID = "validationPolicyId";

    static final Set<String> RESPONSE_DATA_PARAMETERS = new HashSet<>(Arrays.asList(ID_PARAM_NAME, ACTIVE, DESCRIPTION));

}
