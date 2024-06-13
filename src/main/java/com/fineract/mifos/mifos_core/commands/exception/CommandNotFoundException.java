package com.fineract.mifos.mifos_core.commands.exception;

import com.fineract.mifos.mifos_core.infrastructure.core.exception.AbstractPlatformResourceNotFoundException;
/**
 * A {@link RuntimeException} thrown when client resources are not found.
 */
public class CommandNotFoundException extends AbstractPlatformResourceNotFoundException {
    public CommandNotFoundException(final Long id) {
        super("error.msg.command.id.invalid", "Audit with identifier " + id + " does not exist", id);
    }
}
