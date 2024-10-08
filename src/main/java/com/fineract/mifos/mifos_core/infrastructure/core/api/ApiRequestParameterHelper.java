package com.fineract.mifos.mifos_core.infrastructure.core.api;

import com.fineract.mifos.mifos_core.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.MultivaluedMap;
import java.util.Set;

/**
 * <p>
 * Used to process the query parameters provided in a API request to see features of the RESTful API are being asked for
 * such as:
 * </p>
 * <ul>
 * <li>Pretty printing through pretty=true, defaults to false</li>
 * <li>Partial response through fields=id, name etc, when empty, the full data is returned by default.</li>
 * </ul>
 */
@Component
public class ApiRequestParameterHelper {

    public ApiRequestJsonSerializationSettings process(final MultivaluedMap<String, String> queryParameters,
                                                       final Set<String> mandatoryResponseParameters) {

        final Set<String> responseParameters = ApiParameterHelper.extractFieldsForResponseIfProvided(queryParameters);
        if (!responseParameters.isEmpty()) {
            responseParameters.addAll(mandatoryResponseParameters);
        }
        final boolean prettyPrint = ApiParameterHelper.prettyPrint(queryParameters);
        final boolean template = ApiParameterHelper.template(queryParameters);
        final boolean makerCheckerable = ApiParameterHelper.makerCheckerable(queryParameters);
        final boolean includeJson = ApiParameterHelper.includeJson(queryParameters);

        return ApiRequestJsonSerializationSettings.from(prettyPrint, responseParameters, template, makerCheckerable, includeJson);
    }

    public ApiRequestJsonSerializationSettings process(final MultivaluedMap<String, String> queryParameters) {

        final Set<String> responseParameters = ApiParameterHelper.extractFieldsForResponseIfProvided(queryParameters);
        final boolean prettyPrint = ApiParameterHelper.prettyPrint(queryParameters);
        final boolean template = ApiParameterHelper.template(queryParameters);
        final boolean makerCheckerable = ApiParameterHelper.makerCheckerable(queryParameters);
        final boolean includeJson = ApiParameterHelper.includeJson(queryParameters);

        return ApiRequestJsonSerializationSettings.from(prettyPrint, responseParameters, template, makerCheckerable, includeJson);
    }

}
