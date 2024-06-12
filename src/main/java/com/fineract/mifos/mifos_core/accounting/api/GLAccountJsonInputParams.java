package com.fineract.mifos.mifos_core.accounting.api;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

/***
 * Enum of all parameters passed in while creating/updating a GL Account
 ***/

@Getter
public enum GLAccountJsonInputParams {
    ID("id"), NAME("name"), PARENT_ID("parentId"), GL_CODE("glCode"), DISABLED("disabled"), MANUAL_ENTRIES_ALLOWED(
            "manualEntriesAllowed"), TYPE("type"), USAGE("usage"), DESCRIPTION("description"), TAGID("tagId");

    private final String value;

    GLAccountJsonInputParams(final String value) {
        this.value = value;
    }

    private static final Set<String> values = new HashSet<>();

    static {
        for (final GLAccountJsonInputParams type : GLAccountJsonInputParams.values()) {
            values.add(type.value);
        }
    }

    public static Set<String> getAllValues() {
        return values;
    }

    @Override
    public String toString() {
        return name().replaceAll("_", " ");
    }

}
