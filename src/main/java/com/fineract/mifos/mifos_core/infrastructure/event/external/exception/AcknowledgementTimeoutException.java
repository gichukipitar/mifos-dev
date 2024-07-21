package com.fineract.mifos.mifos_core.infrastructure.event.external.exception;

public class AcknowledgementTimeoutException extends RuntimeException {

    public AcknowledgementTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
