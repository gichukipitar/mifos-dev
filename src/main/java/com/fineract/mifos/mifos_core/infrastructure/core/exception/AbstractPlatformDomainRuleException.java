package com.fineract.mifos.mifos_core.infrastructure.core.exception;
/**
 * A {@link RuntimeException} thrown when a valid API request ends up violating some domain rule.
 */
public abstract class AbstractPlatformDomainRuleException extends AbstractPlatformException{
    protected AbstractPlatformDomainRuleException(String globalisationMessageCode, String defaultUserMessage,
                                                  Object... defaultUserMessageArgs) {
        super(globalisationMessageCode, defaultUserMessage, defaultUserMessageArgs);
    }

}
