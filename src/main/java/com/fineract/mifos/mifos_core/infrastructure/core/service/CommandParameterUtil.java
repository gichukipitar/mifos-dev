package com.fineract.mifos.mifos_core.infrastructure.core.service;

import org.apache.commons.lang3.StringUtils;

public final class CommandParameterUtil {

    private CommandParameterUtil() {}

    public static boolean is(final String commandParam, final String commandValue) {
        return StringUtils.isNotBlank(commandParam) && commandParam.trim().equalsIgnoreCase(commandValue);
    }

}
