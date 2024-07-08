package com.fineract.mifos.mifos_core.infrastructure.core.exception;

public class PlatformServiceUnavailableException extends AbstractPlatformServiceUnavailableException {

    public PlatformServiceUnavailableException(String globalisationMessageCode, String defaultUserMessage,
                                               Object... defaultUserMessageArgs) {
        super(globalisationMessageCode, defaultUserMessage, defaultUserMessageArgs);
    }

}
