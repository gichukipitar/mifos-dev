package com.fineract.mifos.mifos_core.infrastructure.codes.exception;

import com.fineract.mifos.mifos_core.infrastructure.core.exception.AbstractPlatformDomainRuleException;


/**
 * A {@link AbstractPlatformDomainRuleException} thrown when someone attempts to update or delete a system defined code.
 */
public class SystemDefinedCodeCannotBeChangedException extends AbstractPlatformDomainRuleException {

    public SystemDefinedCodeCannotBeChangedException() {
        super("error.msg.code.systemdefined", "This code is system defined and cannot be modified or deleted.");
    }

}
