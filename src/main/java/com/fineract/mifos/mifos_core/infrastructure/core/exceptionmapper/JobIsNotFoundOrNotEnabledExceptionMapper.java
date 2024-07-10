package com.fineract.mifos.mifos_core.infrastructure.core.exceptionmapper;

import com.fineract.mifos.mifos_core.infrastructure.core.data.ApiGlobalErrorResponse;
import com.fineract.mifos.mifos_core.infrastructure.core.exception.ErrorHandler;
import com.fineract.mifos.mifos_core.infrastructure.core.exception.JobIsNotFoundOrNotEnabledException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Provider
@Component
@Slf4j
public class JobIsNotFoundOrNotEnabledExceptionMapper implements ExceptionMapper<JobIsNotFoundOrNotEnabledException> {

    @Override
    public Response toResponse(JobIsNotFoundOrNotEnabledException exception) {
        log.warn("Exception occurred", ErrorHandler.findMostSpecificException(exception));
        return Response.status(Response.Status.FORBIDDEN)
                .entity(ApiGlobalErrorResponse.jobIsDisabled(exception.getGlobalisationMessageCode(), exception.getDefaultUserMessage()))
                .type(MediaType.APPLICATION_JSON).build();
    }
}

