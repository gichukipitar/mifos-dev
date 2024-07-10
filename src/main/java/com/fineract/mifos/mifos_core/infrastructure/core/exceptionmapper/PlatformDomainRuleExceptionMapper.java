package com.fineract.mifos.mifos_core.infrastructure.core.exceptionmapper;

import com.fineract.mifos.mifos_core.infrastructure.core.data.ApiGlobalErrorResponse;
import com.fineract.mifos.mifos_core.infrastructure.core.exception.AbstractPlatformDomainRuleException;
import com.fineract.mifos.mifos_core.infrastructure.core.exception.ErrorHandler;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * An {@link ExceptionMapper} to map {@link AbstractPlatformDomainRuleException} thrown by platform into a HTTP API
 * friendly format.
 *
 * The {@link AbstractPlatformDomainRuleException} is thrown when an api call results is some internal business/domain
 * logic been violated.
 */
@Provider
@Component
@Scope("singleton")
@Slf4j
public class PlatformDomainRuleExceptionMapper implements FineractExceptionMapper, ExceptionMapper<AbstractPlatformDomainRuleException> {

    @Override
    public Response toResponse(final AbstractPlatformDomainRuleException exception) {
        log.warn("Exception occurred", ErrorHandler.findMostSpecificException(exception));
        final ApiGlobalErrorResponse notFoundErrorResponse = ApiGlobalErrorResponse.domainRuleViolation(
                exception.getGlobalisationMessageCode(), exception.getDefaultUserMessage(), exception.getDefaultUserMessageArgs());
        // request understood but not carried out due to it violating some
        // domain/business logic
        return Response.status(Response.Status.FORBIDDEN).entity(notFoundErrorResponse).type(MediaType.APPLICATION_JSON).build();
    }

    @Override
    public int errorCode() {
        return 9999;
    }
}

