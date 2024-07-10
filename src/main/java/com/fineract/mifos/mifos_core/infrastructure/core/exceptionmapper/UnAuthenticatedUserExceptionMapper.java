package com.fineract.mifos.mifos_core.infrastructure.core.exceptionmapper;

import com.fineract.mifos.mifos_core.infrastructure.core.data.ApiGlobalErrorResponse;
import com.fineract.mifos.mifos_core.infrastructure.core.exception.ErrorHandler;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * An {@link ExceptionMapper} to map {@link UnAuthenticatedUserException} thrown by platform into a HTTP API friendly
 * format.
 */
@Provider
@Component
@Scope("singleton")
@Slf4j
public class UnAuthenticatedUserExceptionMapper implements ExceptionMapper<UnAuthenticatedUserException> {

    @Override
    public Response toResponse(@SuppressWarnings("unused") final UnAuthenticatedUserException exception) {
        // Status code 401 really reads as: "Unauthenticated":
        log.warn("Exception occurred", ErrorHandler.findMostSpecificException(exception));
        return Response.status(Response.Status.UNAUTHORIZED).entity(ApiGlobalErrorResponse.unAuthenticated()).type(MediaType.APPLICATION_JSON)
                .build();
    }
}

