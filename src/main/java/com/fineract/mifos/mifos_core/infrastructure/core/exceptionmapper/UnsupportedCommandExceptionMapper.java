package com.fineract.mifos.mifos_core.infrastructure.core.exceptionmapper;

import com.fineract.mifos.mifos_core.commands.exception.UnsupportedCommandException;
import com.fineract.mifos.mifos_core.infrastructure.core.data.ApiGlobalErrorResponse;
import com.fineract.mifos.mifos_core.infrastructure.core.dto.ApiParameterError;
import com.fineract.mifos.mifos_core.infrastructure.core.exception.ErrorHandler;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@link ExceptionMapper} to map {@link UnsupportedCommandException} thrown by platform into a HTTP API friendly
 * format.
 */
@Provider
@Component
@Scope("singleton")
@Slf4j
public class UnsupportedCommandExceptionMapper implements ExceptionMapper<UnsupportedCommandException> {

    @Override
    public Response toResponse(final UnsupportedCommandException exception) {
        final List<ApiParameterError> errors = new ArrayList<>();

        final StringBuilder validationErrorCode = new StringBuilder("error.msg.command.unsupported");
        String message = exception.getMessage();
        final StringBuilder defaultEnglishMessage = new StringBuilder("The command ").append(exception.getUnsupportedCommandName())
                .append(" is not supported.");
        if (message != null) {
            defaultEnglishMessage.append(" ").append(message);
        }
        log.warn("Exception occurred", ErrorHandler.findMostSpecificException(exception));
        final ApiParameterError error = ApiParameterError.parameterError(validationErrorCode.toString(), defaultEnglishMessage.toString(),
                exception.getUnsupportedCommandName(), exception.getUnsupportedCommandName());

        errors.add(error);

        final ApiGlobalErrorResponse invalidParameterError = ApiGlobalErrorResponse
                .badClientRequest("validation.msg.validation.errors.exist", "Validation errors exist.", errors);

        return Response.status(Response.Status.BAD_REQUEST).entity(invalidParameterError).type(MediaType.APPLICATION_JSON).build();
    }
}

