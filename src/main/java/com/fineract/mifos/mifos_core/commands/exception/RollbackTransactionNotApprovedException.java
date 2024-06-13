package com.fineract.mifos.mifos_core.commands.exception;

import com.fineract.mifos.mifos_core.infrastructure.core.data.CommandProcessingResult;
import com.fineract.mifos.mifos_core.infrastructure.core.data.CommandProcessingResultBuilder;
import lombok.Getter;

/**
 * When maker-checker is configured globally and also for the current transaction. An initial save determines if there
 * are any integrity rule or data problems. If there isn't... and the transaction is from a maker... then this roll back
 * is issued and the commandSourceResult is used to write the audit entry.
 */

@Getter
public class RollbackTransactionNotApprovedException extends RuntimeException {
    private final CommandProcessingResult result;

    public RollbackTransactionNotApprovedException(Long commandId, Long entityId) {
        this.result = new CommandProcessingResultBuilder().withCommandId(commandId).withEntityId(entityId).setRollbackTransaction(true)
                .build();
    }

}
