package com.fineract.mifos.mifos_core.infrastructure.core.exceptionmapper;

import com.fineract.mifos.mifos_core.infrastructure.core.dto.ApiParameterError;
import com.fineract.mifos.mifos_core.infrastructure.core.exception.ErrorHandler;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.jayway.jsonpath.JsonPathException;

@Provider
@Component
@Scope("singleton")
@Slf4j
public class JsonPathExceptionMapper implements ExceptionMapper<JsonPathException>, FineractExceptionMapper {

    @Override
    public Response toResponse(JsonPathException exception) {
        final String globalisationMessageCode = "error.msg.invalid.json.path";
        final String defaultUserMessage = "The referenced JSON path is invalid.";
        log.warn("Exception occurred", ErrorHandler.findMostSpecificException(exception));

        final ApiParameterError error = ApiParameterError.generalError(globalisationMessageCode, defaultUserMessage);

        return Response.status(Response.Status.BAD_REQUEST).entity(error).type(MediaType.APPLICATION_JSON).build();
    }

    @Override
    public int errorCode() {
        return 4000;
    }
}
