package com.fineract.mifos.mifos_core.infrastructure.core.exception;

import com.fineract.mifos.mifos_core.commands.dtos.CommandWrapper;
import com.fineract.mifos.mifos_core.commands.entity.CommandSource;
import org.antlr.v4.runtime.misc.NotNull;

import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

/**
 * Exception thrown when command is sent with same action, entity and idempotency key
 */
public class IdempotentCommandProcessFailedException extends AbstractIdempotentCommandException{

    private final Integer statusCode;

    public IdempotentCommandProcessFailedException(CommandWrapper wrapper, String idempotencyKey, CommandSource command) {
        super(wrapper.actionName(), wrapper.actionName(), idempotencyKey, command.getResult());
        this.statusCode = command.getResultStatusCode();
    }

    @NotNull
    public Integer getStatusCode() {
        // If the database inconsistent we return http 500 instead of null pointer exception
        return statusCode == null ? Integer.valueOf(SC_INTERNAL_SERVER_ERROR) : statusCode;
    }

}
