package com.fineract.mifos.mifos_core.infrastructure.core.exceptionmapper;

import com.fineract.mifos.mifos_core.infrastructure.core.data.ApiGlobalErrorResponse;
import com.fineract.mifos.mifos_core.infrastructure.core.exception.ErrorHandler;
import jakarta.persistence.OptimisticLockException;
import org.eclipse.persistence.exceptions.OptimisticLockException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

import static jakarta.servlet.http.HttpServletResponse.SC_CONFLICT;

/**
 * An {@link ExceptionMapper} to map {@link ObjectOptimisticLockingFailureException} thrown by platform into a HTTP API
 * friendly format.
 */
@Provider
@Component
@Slf4j
public class OptimisticLockExceptionMapper implements FineractExceptionMapper, ExceptionMapper<OptimisticLockException> {

    @Override
    public Response toResponse(final OptimisticLockException exception) {
        log.warn("Exception occurred", ErrorHandler.findMostSpecificException(exception));
        String type = exception.getQuery() == null ? "unknown" : "query";
        String identifier = "unknown";
        final ApiGlobalErrorResponse dataIntegrityError = ApiGlobalErrorResponse.conflict(type, identifier);
        return Response.status(SC_CONFLICT).entity(dataIntegrityError).type(MediaType.APPLICATION_JSON).build();
    }

    @Override
    public int errorCode() {
        return 4019;
    }
}

