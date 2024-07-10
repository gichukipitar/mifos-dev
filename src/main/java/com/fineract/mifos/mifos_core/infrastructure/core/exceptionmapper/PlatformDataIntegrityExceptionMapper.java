package com.fineract.mifos.mifos_core.infrastructure.core.exceptionmapper;

import com.fineract.mifos.mifos_core.infrastructure.core.data.ApiGlobalErrorResponse;
import com.fineract.mifos.mifos_core.infrastructure.core.exception.ErrorHandler;
import com.fineract.mifos.mifos_core.infrastructure.core.exception.PlatformDataIntegrityException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * An {@link ExceptionMapper} to map {@link PlatformDataIntegrityException} thrown by platform into a HTTP API friendly
 * format.
 *
 * The {@link PlatformDataIntegrityException} is thrown when modifying api call result in data integrity checks to be
 * fired.
 */
@Provider
@Component
@Scope("singleton")
@Slf4j
public class PlatformDataIntegrityExceptionMapper implements FineractExceptionMapper, ExceptionMapper<PlatformDataIntegrityException> {

    @Override
    public Response toResponse(final PlatformDataIntegrityException exception) {
        log.warn("Exception occurred", ErrorHandler.findMostSpecificException(exception));
        final ApiGlobalErrorResponse dataIntegrityError = ApiGlobalErrorResponse.dataIntegrityError(exception.getGlobalisationMessageCode(),
                exception.getDefaultUserMessage(), exception.getParameterName(), exception.getDefaultUserMessageArgs());

        return Response.status(Response.Status.FORBIDDEN).entity(dataIntegrityError).type(MediaType.APPLICATION_JSON).build();
    }

    @Override
    public int errorCode() {
        return 3001;
    }
}

