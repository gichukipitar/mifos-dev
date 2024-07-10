package com.fineract.mifos.mifos_core.infrastructure.core.exceptionmapper;

import com.fineract.mifos.mifos_core.infrastructure.core.data.ApiGlobalErrorResponse;
import com.fineract.mifos.mifos_core.infrastructure.core.exception.ErrorHandler;
import com.fineract.mifos.mifos_core.infrastructure.security.exception.InvalidInstanceTypeMethodException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * An {@link ExceptionMapper} to map {@link InvalidInstanceTypeMethodException} thrown by platform into an HTTP API
 * friendly format.
 */
@Provider
@Component
@Slf4j
public class InvalidInstanceTypeMethodExceptionMapper implements ExceptionMapper<InvalidInstanceTypeMethodException> {

    @Override
    public Response toResponse(final InvalidInstanceTypeMethodException exception) {
        log.warn("Exception occurred", ErrorHandler.findMostSpecificException(exception));
        ApiGlobalErrorResponse errorResponse = ApiGlobalErrorResponse.invalidInstanceTypeMethod(exception.getMethod());
        return Response.status(Response.Status.METHOD_NOT_ALLOWED).entity(errorResponse).type(MediaType.APPLICATION_JSON).build();
    }
}
