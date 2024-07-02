package com.fineract.mifos.mifos_core.infrastructure.core.api;

import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.core.MultivaluedMap;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public final class ApiParameterHelper {

    private ApiParameterHelper() {

    }

    public static Long commandId(final MultivaluedMap<String, String> queryParams) {
        Long id = null;
        if (queryParams.getFirst("commandId") != null) {
            final String value = queryParams.getFirst("commandId");
            if (StringUtils.isNotBlank(value)) {
                id = Long.valueOf(value);
            }
        }
        return id;
    }

    public static Set<String> extractFieldsForResponseIfProvided(final MultivaluedMap<String, String> queryParams) {
        Set<String> fields = new HashSet<>();
        String commaSeparatedParameters = "";
        if (queryParams.getFirst("fields") != null) {
            commaSeparatedParameters = queryParams.getFirst("fields");
            if (StringUtils.isNotBlank(commaSeparatedParameters)) {
                fields = new HashSet<>(Arrays.asList(commaSeparatedParameters.split("\\s*,\\s*"))); // NOSONAR
            }
        }
        return fields;
    }

    public static Set<String> extractAssociationsForResponseIfProvided(final MultivaluedMap<String, String> queryParams) {
        Set<String> fields = new HashSet<>();
        String commaSeparatedParameters = "";
        if (queryParams.getFirst("associations") != null) {
            commaSeparatedParameters = queryParams.getFirst("associations");
            if (StringUtils.isNotBlank(commaSeparatedParameters)) {
                fields = new HashSet<>(Arrays.asList(commaSeparatedParameters.split("\\s*,\\s*"))); // NOSONAR
            }
        }
        return fields;
    }

    public static void excludeAssociationsForResponseIfProvided(final String commaSeparatedParameters, Set<String> fields) {
        if (StringUtils.isNotBlank(commaSeparatedParameters)) {
            fields.removeAll(new HashSet<>(Arrays.asList(commaSeparatedParameters.split("\\s*,\\s*")))); // NOSONAR
        }
    }

    public static void excludeAssociationsForResponseIfProvided(final MultivaluedMap<String, String> queryParams, Set<String> fields) {
        if (queryParams.getFirst("exclude") != null) {
            excludeAssociationsForResponseIfProvided(queryParams.getFirst("exclude"), fields);
        }
    }

    public static boolean prettyPrint(final MultivaluedMap<String, String> queryParams) {
        boolean prettyPrint = false;
        if (queryParams.getFirst("pretty") != null) {
            final String prettyPrintValue = queryParams.getFirst("pretty");
            prettyPrint = "true".equalsIgnoreCase(prettyPrintValue);
        }
        return prettyPrint;
    }

    public static Locale extractLocale(final MultivaluedMap<String, String> queryParams) {
        Locale locale = null;
        if (queryParams.getFirst("locale") != null) {
            final String localeAsString = queryParams.getFirst("locale");
            locale = JsonParserHelper.localeFromString(localeAsString);
        }
        return locale;
    }

    public static boolean parameterType(final MultivaluedMap<String, String> queryParams) {
        boolean parameterType = false;
        if (queryParams.getFirst("parameterType") != null) {
            final String parameterTypeValue = queryParams.getFirst("parameterType");
            parameterType = "true".equalsIgnoreCase(parameterTypeValue);
        }
        return parameterType;
    }

    public static boolean template(final MultivaluedMap<String, String> queryParams) {
        boolean template = false;
        if (queryParams.getFirst("template") != null) {
            final String prettyPrintValue = queryParams.getFirst("template");
            template = "true".equalsIgnoreCase(prettyPrintValue);
        }
        return template;
    }

    public static boolean makerCheckerable(final MultivaluedMap<String, String> queryParams) {
        boolean makerCheckerable = false;
        if (queryParams.getFirst("makerCheckerable") != null) {
            final String prettyPrintValue = queryParams.getFirst("makerCheckerable");
            makerCheckerable = "true".equalsIgnoreCase(prettyPrintValue);
        }
        return makerCheckerable;
    }

    public static boolean includeJson(final MultivaluedMap<String, String> queryParams) {
        boolean includeJson = false;
        if (queryParams.getFirst("includeJson") != null) {
            final String includeJsonValue = queryParams.getFirst("includeJson");
            includeJson = "true".equalsIgnoreCase(includeJsonValue);
        }
        return includeJson;
    }

    public static boolean genericResultSet(final MultivaluedMap<String, String> queryParams) {
        boolean genericResultSet = false;
        if (queryParams.getFirst("genericResultSet") != null) {
            final String genericResultSetValue = queryParams.getFirst("genericResultSet");
            genericResultSet = "true".equalsIgnoreCase(genericResultSetValue);
        }
        return genericResultSet;
    }

    public static boolean genericResultSetPassed(final MultivaluedMap<String, String> queryParams) {
        return queryParams.getFirst("genericResultSet") != null;
    }

}
