package com.fineract.mifos.mifos_core.infrastructure.event.external.exception;

public class ExternalEventConfigurationNotFoundException extends RuntimeException {

    public ExternalEventConfigurationNotFoundException() {
        super("No external events configured");
    }

    public ExternalEventConfigurationNotFoundException(final String externalEventType) {
        super("Configuration not found for external event " + externalEventType);
    }
}
