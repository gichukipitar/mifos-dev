package com.fineract.mifos.mifos_core.batch.command;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandStrategyUtils {
    private static final Pattern VERSIONED_RELATIVE_URL_PATTERN = Pattern.compile("^(v[1-9][0-9]*/)(.*)$");

    private CommandStrategyUtils() {

    }
    /**
     * Get query parameters from relative URL.
     *
     * @param relativeUrl
     *            the relative URL
     * @return the query parameters in a map
     */

    public static Map<String, String> getQueryParameters(final String relativeUrl) {
        final String queryParameterStr = StringUtils.substringAfter(relativeUrl, "?");
        final String[] queryParametersArray = StringUtils.split(queryParameterStr, "&");
        final Map<String, String> queryParametersMap = new HashMap<>();
        for (String parameterStr : queryParametersArray) {
            String[] keyValue = StringUtils.split(parameterStr, "=");
            queryParametersMap.put(keyValue[0], keyValue[1]);
        }
        return queryParametersMap;
    }

    /**
     * Add query parameters(received in the relative URL) to URI info query parameters.
     *
     * @param uriInfo
     *            the URI info
     * @param queryParameters
     *            the query parameters
     */
    public static void addQueryParametersToUriInfo(final MutableUriInfo uriInfo, final Map<String, String> queryParameters) {
        for (Map.Entry<String, String> entry : queryParameters.entrySet()) {
            uriInfo.addAdditionalQueryParameter(entry.getKey(), entry.getValue());
        }
    }

    public static String relativeUrlWithoutVersion(BatchRequest request) {
        String relativeUrl = request.getRelativeUrl();
        Matcher m = VERSIONED_RELATIVE_URL_PATTERN.matcher(relativeUrl);
        if (m.matches()) {
            return m.group(2);
        } else {
            return relativeUrl;
        }
    }

    public static boolean isResourceVersioned(CommandContext commandContext) {
        String relativeUrl = commandContext.getResource();
        return isRelativeUrlVersioned(relativeUrl);
    }

    public static boolean isRelativeUrlVersioned(String relativeUrl) {
        Matcher m = VERSIONED_RELATIVE_URL_PATTERN.matcher(relativeUrl);
        return m.matches();
    }
}
