package com.fineract.mifos.mifos_core.infrastructure.core.exceptionmapper;

import com.fineract.mifos.mifos_core.infrastructure.core.data.ApiGlobalErrorResponse;
import com.fineract.mifos.mifos_core.infrastructure.core.exception.ErrorHandler;
import com.fineract.mifos.mifos_core.infrastructure.core.exception.PlatformApiDataValidationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * An {@link ExceptionMapper} to map {@link PlatformApiDataValidationException} thrown by platform into a HTTP API
 * friendly format.
 *
 * The {@link PlatformApiDataValidationException} is typically thrown in data validation of the parameters passed in
 * with an api request.
 */
@Provider
@Component
@Scope("singleton")
@Slf4j
public class PlatformApiDataValidationExceptionMapper
        implements FineractExceptionMapper, ExceptionMapper<PlatformApiDataValidationException> {

    @Override
    public Response toResponse(final PlatformApiDataValidationException exception) {
        log.warn("Exception occurred", ErrorHandler.findMostSpecificException(exception));
        final ApiGlobalErrorResponse dataValidationErrorResponse = ApiGlobalErrorResponse
                .badClientRequest(exception.getGlobalisationMessageCode(), exception.getDefaultUserMessage(), exception.getErrors());

        return Response.status(Response.Status.BAD_REQUEST).entity(dataValidationErrorResponse).type(MediaType.APPLICATION_JSON).build();
    }

    @Override
    public int errorCode() {
        return 2002;
    }
}
