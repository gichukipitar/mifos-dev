package com.fineract.mifos.mifos_core.infrastructure.core.exceptionmapper;

import com.fineract.mifos.mifos_core.infrastructure.core.exception.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.fineract.mifos.mifos_core.infrastructure.core.exception.AbstractIdempotentCommandException.IDEMPOTENT_CACHE_HEADER;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;

/**
 * An {@link ExceptionMapper} to map {@link } thrown by platform into a HTTP API friendly format.
 */
@Provider
@Component
@Scope("singleton")
@Slf4j
public class IdempotentCommandExceptionMapper implements FineractExceptionMapper, ExceptionMapper<AbstractIdempotentCommandException> {

    @Override
    public Response toResponse(final AbstractIdempotentCommandException exception) {
        log.warn("Exception occurred", ErrorHandler.findMostSpecificException(exception));
        Integer status = null;
        if (exception instanceof IdempotentCommandProcessSucceedException pse) {
            Integer statusCode = pse.getStatusCode();
            status = statusCode == null ? SC_OK : statusCode;
        }
        if (exception instanceof IdempotentCommandProcessUnderProcessingException) {
            status = 425;
        } else if (exception instanceof IdempotentCommandProcessFailedException pfe) {
            status = pfe.getStatusCode();
        }
        if (status == null) {
            status = SC_INTERNAL_SERVER_ERROR;
        }
        return Response.status(status).entity(exception.getResponse()).header(IDEMPOTENT_CACHE_HEADER, "true")
                .type(MediaType.APPLICATION_JSON).build();
    }

    @Override
    public int errorCode() {
        return 4209;
    }
}
