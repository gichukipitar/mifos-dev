package com.fineract.mifos.mifos_core.infrastructure.core.exception;

/**
 * A {@link RuntimeException} thrown for cases such as 3rd-party external services that are not available.
 */
public abstract class AbstractPlatformServiceUnavailableException extends AbstractPlatformException {

    protected AbstractPlatformServiceUnavailableException(String globalisationMessageCode, String defaultUserMessage,
                                                          Object... defaultUserMessageArgs) {
        super(globalisationMessageCode, defaultUserMessage, defaultUserMessageArgs);
    }
}
