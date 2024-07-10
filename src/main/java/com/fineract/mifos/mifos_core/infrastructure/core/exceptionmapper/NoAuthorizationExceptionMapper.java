package com.fineract.mifos.mifos_core.infrastructure.core.exceptionmapper;

import com.fineract.mifos.mifos_core.infrastructure.core.data.ApiGlobalErrorResponse;
import com.fineract.mifos.mifos_core.infrastructure.core.exception.ErrorHandler;
import com.fineract.mifos.mifos_core.infrastructure.security.exception.NoAuthorizationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * An {@link ExceptionMapper} to map {@link NoAuthorizationException} thrown by platform into a HTTP API friendly
 * format.
 *
 * The {@link NoAuthorizationException} is thrown on platform when an attempt is made to use functionality for which the
 * user does have sufficient privileges.
 */
@Provider
@Component
@Scope("singleton")
@Slf4j
public class NoAuthorizationExceptionMapper implements ExceptionMapper<NoAuthorizationException> {

    @Override
    public Response toResponse(final NoAuthorizationException exception) {
        // Status code 403 really reads as:
        // "Authenticated - but not authorized":
        final String defaultUserMessage = exception.getMessage();
        log.warn("Exception occurred", ErrorHandler.findMostSpecificException(exception));
        return Response.status(Response.Status.FORBIDDEN).entity(ApiGlobalErrorResponse.unAuthorized(defaultUserMessage))
                .type(MediaType.APPLICATION_JSON).build();
    }
}
