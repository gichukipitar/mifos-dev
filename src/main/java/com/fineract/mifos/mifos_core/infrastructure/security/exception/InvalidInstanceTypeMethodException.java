package com.fineract.mifos.mifos_core.infrastructure.security.exception;

import com.fineract.mifos.mifos_core.infrastructure.core.exception.AbstractPlatformDomainRuleException;
import lombok.Getter;

/**
 * {@link RuntimeException} thrown when an invalid method in the Fineract instance type called in request to platform.
 *
 *
 */
@Getter
public class InvalidInstanceTypeMethodException extends AbstractPlatformDomainRuleException {
    private final String method;

    public InvalidInstanceTypeMethodException(final String method) {
        super("error.msg.invalid.method.for.instance.type", "Method Not Allowed " + method + " for the instance type");
        this.method = method;
    }
}
