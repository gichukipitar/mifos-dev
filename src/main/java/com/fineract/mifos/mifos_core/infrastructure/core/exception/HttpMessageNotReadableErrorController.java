package com.fineract.mifos.mifos_core.infrastructure.core.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import com.fineract.mifos.mifos_core.infrastructure.core.dto.ApiParameterError;
@Provider
@Component
@Scope("singleton")
@Slf4j
public class HttpMessageNotReadableErrorController implements ExceptionMapper<HttpMessageNotReadableException>, FineractExceptionMapper{

    @Override
    public Response toResponse(HttpMessageNotReadableException exception) {
        final String globalisationMessageCode = "error.msg.invalid.json.data";
        final String defaultUserMessage = "The referenced JSON data is invalid, validate date format as yyyy-MM-dd or other cases like String instead of Number";
        log.warn("Exception occurred", ErrorHandler.findMostSpecificException(exception));

        final ApiParameterError error = ApiParameterError.generalError(globalisationMessageCode, defaultUserMessage);

        return Response.status(Response.Status.BAD_REQUEST).entity(error).type(MediaType.APPLICATION_JSON).build();
    }

    @Override
    public int errorCode() {
        return 4001;
    }

}
