package com.fineract.mifos.mifos_core.infrastructure.core.exceptionmapper;

import com.fineract.mifos.mifos_core.cob.exceptions.BusinessStepNotBelongsToJobException;
import com.fineract.mifos.mifos_core.infrastructure.core.dto.ApiParameterError;
import com.fineract.mifos.mifos_core.infrastructure.core.exception.ErrorHandler;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Provider
@Component
@Slf4j
public class BusinessStepNotBelongsToJobExceptionMapper implements ExceptionMapper<BusinessStepNotBelongsToJobException> {

    @Override
    public Response toResponse(BusinessStepNotBelongsToJobException exception) {
        final String globalisationMessageCode = "error.msg.invalid.request.body";
        final String defaultUserMessage = "One of the provided Business Steps does not belong to the provided Job Name.";
        log.warn("Exception occurred", ErrorHandler.findMostSpecificException(exception));

        final ApiParameterError error = ApiParameterError.generalError(globalisationMessageCode, defaultUserMessage);

        return Response.status(Response.Status.BAD_REQUEST).entity(error).type(MediaType.APPLICATION_JSON).build();
    }

}
