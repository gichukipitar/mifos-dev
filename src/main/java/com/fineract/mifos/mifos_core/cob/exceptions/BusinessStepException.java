package com.fineract.mifos.mifos_core.cob.exceptions;

public class BusinessStepException extends RuntimeException {
    public BusinessStepException(String message) {
        super(message);
    }

    public BusinessStepException(String message, Throwable t) {
        super(message, t);
    }
}
