package com.fineract.mifos.mifos_core.infrastructure.core.exceptionmapper;

import com.fineract.mifos.mifos_core.infrastructure.core.data.ApiGlobalErrorResponse;
import com.fineract.mifos.mifos_core.infrastructure.core.exception.ErrorHandler;
import com.fineract.mifos.mifos_core.infrastructure.core.exception.PlatformRequestBodyItemLimitValidationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Provider
@Component
@Scope("singleton")
@Slf4j
public class PlatformRequestBodyItemLimitValidationExceptionMapper
        implements ExceptionMapper<PlatformRequestBodyItemLimitValidationException> {

    @Override
    public Response toResponse(PlatformRequestBodyItemLimitValidationException exception) {
        String globalisationMessage = "error.msg.validation.request.body.item.limit.validation";
        log.warn("Exception occurred", ErrorHandler.findMostSpecificException(exception));
        final ApiGlobalErrorResponse badRequestErrorResponse = ApiGlobalErrorResponse.badClientRequest(globalisationMessage,
                exception.getMessage());
        return Response.status(Response.Status.BAD_REQUEST).entity(badRequestErrorResponse).type(MediaType.APPLICATION_JSON).build();
    }
}
