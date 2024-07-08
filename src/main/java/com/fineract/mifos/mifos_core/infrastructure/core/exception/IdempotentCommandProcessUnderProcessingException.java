package com.fineract.mifos.mifos_core.infrastructure.core.exception;

import com.fineract.mifos.mifos_core.commands.dtos.CommandWrapper;

/**
 * Exception thrown when command is sent with same action, entity and idempotency key
 */
public class IdempotentCommandProcessUnderProcessingException extends AbstractIdempotentCommandException {

    public IdempotentCommandProcessUnderProcessingException(CommandWrapper wrapper, String idempotencyKey) {
        super(wrapper.actionName(), wrapper.entityName(), idempotencyKey, wrapper.getJson());
    }

}
