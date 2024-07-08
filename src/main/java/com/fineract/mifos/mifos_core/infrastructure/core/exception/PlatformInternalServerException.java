package com.fineract.mifos.mifos_core.infrastructure.core.exception;

/**
 * A {@link RuntimeException} thrown when unexpected server side errors happen.
 */
public class PlatformInternalServerException extends AbstractPlatformException {

    public PlatformInternalServerException(String globalisationMessageCode, String defaultUserMessage, Object... defaultUserMessageArgs) {
        super(globalisationMessageCode, defaultUserMessage, defaultUserMessageArgs);
    }
}
