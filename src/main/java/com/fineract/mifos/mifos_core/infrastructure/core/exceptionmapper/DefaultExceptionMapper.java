package com.fineract.mifos.mifos_core.infrastructure.core.exceptionmapper;

import com.fineract.mifos.mifos_core.infrastructure.core.exception.ErrorHandler;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;

import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

@Component
@Scope("singleton")
@Slf4j
public class DefaultExceptionMapper implements FineractExceptionMapper, ExceptionMapper<RuntimeException> {

    @Override
    public int errorCode() {
        return 9999;
    }

    @Override
    public Response toResponse(RuntimeException exception) {
        log.warn("Exception occurred", ErrorHandler.findMostSpecificException(exception));
        return Response.status(SC_INTERNAL_SERVER_ERROR)
                .entity(Map.of("Exception", ObjectUtils.defaultIfNull(exception.getMessage(), "No error message available")))
                .type(MediaType.APPLICATION_JSON).build();
    }
}

