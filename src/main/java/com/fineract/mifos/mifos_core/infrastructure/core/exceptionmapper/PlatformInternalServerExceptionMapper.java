package com.fineract.mifos.mifos_core.infrastructure.core.exceptionmapper;

import com.fineract.mifos.mifos_core.infrastructure.core.data.ApiGlobalErrorResponse;
import com.fineract.mifos.mifos_core.infrastructure.core.exception.ErrorHandler;
import com.fineract.mifos.mifos_core.infrastructure.core.exception.PlatformInternalServerException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * An {@link ExceptionMapper} to map {@link PlatformInternalServerException} thrown by platform into a HTTP API friendly
 * format.
 *
 * The {@link PlatformInternalServerException} is thrown when an api call results in unexpected server side exceptions.
 */
@Provider
@Component
@Scope("singleton")
@Slf4j
public class PlatformInternalServerExceptionMapper implements FineractExceptionMapper, ExceptionMapper<PlatformInternalServerException> {

    @Override
    public Response toResponse(final PlatformInternalServerException exception) {
        log.warn("Exception occurred", ErrorHandler.findMostSpecificException(exception));
        final ApiGlobalErrorResponse notFoundErrorResponse = ApiGlobalErrorResponse.serverSideError(exception.getGlobalisationMessageCode(),
                exception.getDefaultUserMessage(), exception.getDefaultUserMessageArgs());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(notFoundErrorResponse).type(MediaType.APPLICATION_JSON).build();
    }

    @Override
    public int errorCode() {
        return 5001;
    }
}

