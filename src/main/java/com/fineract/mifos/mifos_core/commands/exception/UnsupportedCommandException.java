package com.fineract.mifos.mifos_core.commands.exception;

import lombok.Data;
import lombok.Getter;

/**
 * A {@link RuntimeException} that is thrown in the case where an invalid or unknown command is attempted to be
 * processed by platform.
 */
@Getter
@Data
public class UnsupportedCommandException extends RuntimeException {
    private final String unsupportedCommandName;

    public UnsupportedCommandException(final String unsupportedCommandName) {
        this.unsupportedCommandName = unsupportedCommandName;
    }

    public UnsupportedCommandException(final String unsupportedCommandName, String message) {
        super(message);
        this.unsupportedCommandName = unsupportedCommandName;
    }

}
