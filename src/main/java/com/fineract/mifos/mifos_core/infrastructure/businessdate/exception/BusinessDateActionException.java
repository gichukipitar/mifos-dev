package com.fineract.mifos.mifos_core.infrastructure.businessdate.exception;

import com.fineract.mifos.mifos_core.infrastructure.core.exception.AbstractPlatformDomainRuleException;

/**
 * A {@link RuntimeException} thrown when business date is violating any domain rule.
 */

public class BusinessDateActionException extends AbstractPlatformDomainRuleException {
    public BusinessDateActionException(final String violationCode, String message) {
        super(violationCode, message, violationCode);
    }
}
