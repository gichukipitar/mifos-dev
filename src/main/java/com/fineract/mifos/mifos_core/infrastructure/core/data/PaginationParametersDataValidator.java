package com.fineract.mifos.mifos_core.infrastructure.core.data;

import com.fineract.mifos.mifos_core.infrastructure.core.dto.ApiParameterError;
import com.fineract.mifos.mifos_core.infrastructure.core.exception.PlatformApiDataValidationException;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PaginationParametersDataValidator {

    private static final Set<String> sortOrderValues = new HashSet<>(Arrays.asList("ASC", "DESC"));

    public void validateParameterValues(PaginationParameters parameters, final Set<String> supportedOrdeByValues,
                                        final String resourceName) {
        final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
        if (parameters.isOrderByRequested() && !supportedOrdeByValues.contains(parameters.getOrderBy())) {
            final String defaultUserMessage = "The orderBy value '" + parameters.getOrderBy()
                    + "' is not supported. The supported orderBy values are " + supportedOrdeByValues;
            final ApiParameterError error = ApiParameterError.parameterError(
                    "validation.msg." + resourceName + ".orderBy.value.is.not.supported", defaultUserMessage, "orderBy",
                    parameters.getOrderBy(), supportedOrdeByValues.toString());
            dataValidationErrors.add(error);
        }

        if (parameters.isSortOrderProvided() && !sortOrderValues.contains(parameters.getSortOrder().toUpperCase())) {
            final String defaultUserMessage = "The sortOrder value '" + parameters.getSortOrder()
                    + "' is not supported. The supported sortOrder values are " + sortOrderValues;
            final ApiParameterError error = ApiParameterError.parameterError(
                    "validation.msg." + resourceName + ".sortOrder.value.is.not.supported", defaultUserMessage, "sortOrder",
                    parameters.getSortOrder(), sortOrderValues.toString());
            dataValidationErrors.add(error);
        }
        throwExceptionIfValidationWarningsExist(dataValidationErrors);
    }

    private void throwExceptionIfValidationWarningsExist(final List<ApiParameterError> dataValidationErrors) {
        if (!dataValidationErrors.isEmpty()) {
            throw new PlatformApiDataValidationException(dataValidationErrors);
        }
    }

}
