package com.fineract.mifos.mifos_core.infrastructure.core.exceptionmapper;

import com.fineract.mifos.mifos_core.commands.exception.RollbackTransactionNotApprovedException;
import com.fineract.mifos.mifos_core.infrastructure.core.exception.ErrorHandler;
import com.google.gson.Gson;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * An {@link ExceptionMapper} to map {@link } thrown by platform into a HTTP API
 * friendly format.
 *
 * The {@link } is typically thrown in data validation of the parameters passed in
 * with an api request.
 */
@Provider
@Component
@Scope("singleton")
@Slf4j
public class RollbackTransactionNotApprovedExceptionMapper
        implements FineractExceptionMapper, ExceptionMapper<RollbackTransactionNotApprovedException> {

    @Override
    public Response toResponse(final RollbackTransactionNotApprovedException exception) {
        log.warn("Exception occurred", ErrorHandler.findMostSpecificException(exception));
        return Response.ok().entity(new Gson().toJson(exception.getResult())).type(MediaType.APPLICATION_JSON).build();
    }

    @Override
    public int errorCode() {
        return 2000;
    }
}
