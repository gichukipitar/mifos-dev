package com.fineract.mifos.mifos_core.commands.exception;

import com.fineract.mifos.mifos_core.infrastructure.core.exception.AbstractPlatformResourceNotFoundException;
/**
 * A {@link RuntimeException} thrown when client resources are not found.
 */
public class CommandNotAwaitingApprovalException extends AbstractPlatformResourceNotFoundException {
    public CommandNotAwaitingApprovalException(final Long id) {
        super("error.msg.command.id.not.awaiting.approval", "Audit with identifier " + id + " is Not Awaiting Approval", id);
    }
}
