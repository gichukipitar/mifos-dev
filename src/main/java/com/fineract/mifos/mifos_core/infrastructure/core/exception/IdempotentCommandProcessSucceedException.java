package com.fineract.mifos.mifos_core.infrastructure.core.exception;

import com.fineract.mifos.mifos_core.commands.dtos.CommandWrapper;
import com.fineract.mifos.mifos_core.commands.entity.CommandSource;
import lombok.Getter;

/**
 * Exception thrown when command is sent with same action, entity and idempotency key
 */
@Getter
public class IdempotentCommandProcessSucceedException extends AbstractIdempotentCommandException{

    private final Integer statusCode;

    public IdempotentCommandProcessSucceedException(CommandWrapper wrapper, String idempotencyKey, CommandSource command) {
        super(wrapper.actionName(), wrapper.entityName(), idempotencyKey, command.getResult());
        this.statusCode = command.getResultStatusCode();
    }

}
