package com.fineract.mifos.mifos_core.infrastructure.core.exception;
/**
 * A {@link RuntimeException} thrown when resources that are queried for are not found.
 */
public class AbstractPlatformResourceNotFoundException extends AbstractPlatformException {

    protected AbstractPlatformResourceNotFoundException(final String globalisationMessageCode, final String defaultUserMessage,
                                                        final Object... defaultUserMessageArgs) {
        super(globalisationMessageCode, defaultUserMessage, defaultUserMessageArgs);
    }
}