package com.fineract.mifos.mifos_core.infrastructure.core.exceptionmapper;

import com.fineract.mifos.mifos_core.infrastructure.core.data.ApiGlobalErrorResponse;
import com.fineract.mifos.mifos_core.infrastructure.core.exception.AbstractPlatformResourceNotFoundException;
import com.fineract.mifos.mifos_core.infrastructure.core.exception.ErrorHandler;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * An {@link ExceptionMapper} to map {@link AbstractPlatformResourceNotFoundException} thrown by platform into a HTTP
 * API friendly format.
 *
 * The {@link AbstractPlatformResourceNotFoundException} is thrown when an api call for a resource that is expected to
 * exist does not.
 */
@Provider
@Component
@Scope("singleton")
@Slf4j
public class PlatformResourceNotFoundExceptionMapper
        implements FineractExceptionMapper, ExceptionMapper<AbstractPlatformResourceNotFoundException> {

    @Override
    public Response toResponse(final AbstractPlatformResourceNotFoundException exception) {
        log.warn("Exception occurred", ErrorHandler.findMostSpecificException(exception));
        final ApiGlobalErrorResponse notFoundErrorResponse = ApiGlobalErrorResponse.notFound(exception.getGlobalisationMessageCode(),
                exception.getDefaultUserMessage(), exception.getDefaultUserMessageArgs());
        return Response.status(Response.Status.NOT_FOUND).entity(notFoundErrorResponse).type(MediaType.APPLICATION_JSON).build();
    }

    @Override
    public int errorCode() {
        return 1001;
    }
}

