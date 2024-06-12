package com.fineract.mifos.mifos_core.batch.exception;

import com.fineract.mifos.mifos_core.infrastructure.core.exception.AbstractPlatformDomainRuleException;

public class BatchReferenceInvalidException extends AbstractPlatformDomainRuleException {
    public BatchReferenceInvalidException() {
        super("validation.msg.batch.root.invalid", "Root request not found");
    }

    public BatchReferenceInvalidException(Long reference) {
        super("validation.msg.batch.reference.invalid", "Referenced request not found", reference);
    }
}
