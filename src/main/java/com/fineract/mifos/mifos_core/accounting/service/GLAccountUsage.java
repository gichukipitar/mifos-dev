package com.fineract.mifos.mifos_core.accounting.service;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;


public enum GLAccountUsage {
    DETAIL(1, "accountUsage.detail"), HEADER(2, "accountUsage.header");

    @Getter
    private final Integer value;
    @Getter
    private final String code;

    GLAccountUsage(final Integer value, final String code) {
        this.value = value;
        this.code = code;
    }

    private static final Map<Integer, GLAccountUsage> intToEnumMap = new HashMap<>();
    @Getter
    private static int minValue;
    @Getter
    private static int maxValue;

    static {
        int i = 0;
        for (final GLAccountUsage type : GLAccountUsage.values()) {
            if (i == 0) {
                minValue = type.value;
            }
            intToEnumMap.put(type.value, type);
            if (minValue >= type.value) {
                minValue = type.value;
            }
            if (maxValue < type.value) {
                maxValue = type.value;
            }
            i = i + 1;
        }
    }

    public static GLAccountUsage fromInt(final int i) {
        final GLAccountUsage type = intToEnumMap.get(Integer.valueOf(i));
        return type;
    }

    @Override
    public String toString() {
        return name().toString();
    }


}
