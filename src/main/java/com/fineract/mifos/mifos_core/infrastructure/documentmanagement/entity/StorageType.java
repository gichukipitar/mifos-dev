package com.fineract.mifos.mifos_core.infrastructure.documentmanagement.entity;

import java.util.HashMap;
import java.util.Map;

public enum StorageType {

    FILE_SYSTEM(1), S3(2);

    private final Integer value;

    StorageType(final Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }

    private static final Map<Integer, StorageType> intToEnumMap = new HashMap<>();

    static {
        for (final StorageType type : StorageType.values()) {
            intToEnumMap.put(type.value, type);
        }
    }

    public static StorageType fromInt(final int i) {
        return intToEnumMap.get(i);
    }
}
