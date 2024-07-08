package com.fineract.mifos.mifos_core.infrastructure.core.exception;

import lombok.Getter;

/**
 * A {@link RuntimeException} thrown when data integrity problems happen due to state modifying actions.
 */
@Getter
public class PlatformDataIntegrityException extends AbstractPlatformException {

    private final String parameterName;

    public PlatformDataIntegrityException(final String globalisationMessageCode, final String defaultUserMessage,
                                          final Object... defaultUserMessageArgs) {
        super(globalisationMessageCode, defaultUserMessage, defaultUserMessageArgs);
        this.parameterName = null;
    }

    public PlatformDataIntegrityException(final String globalisationMessageCode, final String defaultUserMessage,
                                          final String parameterName, final Object... defaultUserMessageArgs) {
        super(globalisationMessageCode, defaultUserMessage, defaultUserMessageArgs);
        this.parameterName = parameterName;
    }

}
