package com.fineract.mifos.mifos_core.infrastructure.core.exceptionmapper;

import com.fineract.mifos.mifos_core.infrastructure.core.data.ApiGlobalErrorResponse;
import com.fineract.mifos.mifos_core.infrastructure.core.dto.ApiParameterError;
import com.fineract.mifos.mifos_core.infrastructure.core.exception.ErrorHandler;
import com.fineract.mifos.mifos_core.infrastructure.core.exception.UnsupportedParameterException;
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
 * An {@link ExceptionMapper} to map {@link UnsupportedParameterException} thrown by platform into a HTTP API friendly
 * format.
 */
@Provider
@Component
@Scope("singleton")
@Slf4j
public class UnsupportedParameterExceptionMapper implements FineractExceptionMapper, ExceptionMapper<UnsupportedParameterException> {

    @Override
    public Response toResponse(final UnsupportedParameterException exception) {
        final List<ApiParameterError> errors = new ArrayList<>();

        for (final String parameterName : exception.getUnsupportedParameters()) {
            String defaultEnglishMessage = "The parameter " + parameterName +
                    " is not supported.";
            final ApiParameterError error = ApiParameterError.parameterError("error.msg.parameter.unsupported",
                    defaultEnglishMessage, parameterName, parameterName);

            errors.add(error);
        }
        log.warn("Exception occurred", ErrorHandler.findMostSpecificException(exception));

        final ApiGlobalErrorResponse invalidParameterError = ApiGlobalErrorResponse
                .badClientRequest("validation.msg.validation.errors.exist", "Validation errors exist.", errors);

        return Response.status(Response.Status.BAD_REQUEST).entity(invalidParameterError).type(MediaType.APPLICATION_JSON).build();
    }

    @Override
    public int errorCode() {
        return 2001;
    }
}

