package com.fineract.mifos.mifos_core.infrastructure.core.exceptionmapper;

import com.fineract.mifos.mifos_core.infrastructure.core.data.ApiGlobalErrorResponse;
import com.fineract.mifos.mifos_core.infrastructure.core.dto.ApiParameterError;
import com.fineract.mifos.mifos_core.infrastructure.core.exception.ErrorHandler;
import com.fineract.mifos.mifos_core.infrastructure.core.exception.UnrecognizedQueryParamException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@link ExceptionMapper} to map {@link UnrecognizedQueryParamException} thrown by platform into a HTTP API friendly
 * format.
 *
 * The {@link UnrecognizedQueryParamException} is typically thrown when a parameter is passed during and post or put
 * that is not expected.
 */
@Provider
@Component
@Scope("singleton")
@Slf4j
public class UnrecognizedQueryParamExceptionMapper implements ExceptionMapper<UnrecognizedQueryParamException> {

    @Override
    public Response toResponse(final UnrecognizedQueryParamException exception) {
        final String parameterName = exception.getQueryParamKey();
        final String parameterValue = exception.getQueryParamValue();

        String validationErrorCode = "error.msg.query.parameter.value.unsupported";
        String defaultEnglishMessage = "The query parameter " + //
                parameterName + //
                " has an unsupported value of: " + //
                parameterValue;
        log.warn("Exception occurred", ErrorHandler.findMostSpecificException(exception));

        final ApiParameterError error = ApiParameterError.parameterError(validationErrorCode, defaultEnglishMessage,
                parameterName, parameterName, parameterValue, exception.getSupportedParams());

        final List<ApiParameterError> errors = new ArrayList<>();
        errors.add(error);

        final ApiGlobalErrorResponse invalidParameterError = ApiGlobalErrorResponse
                .badClientRequest("validation.msg.validation.errors.exist", "Validation errors exist.", errors);

        return Response.status(Response.Status.BAD_REQUEST).entity(invalidParameterError).type(MediaType.APPLICATION_JSON).build();
    }
}

