package com.fineract.mifos.mifos_core.infrastructure.businessdate.exception;

import com.fineract.mifos.mifos_core.infrastructure.core.exception.AbstractPlatformResourceNotFoundException;

/**
 * A {@link RuntimeException} thrown when business date is not found.
 */

public class BusinessDateNotFoundException extends AbstractPlatformResourceNotFoundException {

    public BusinessDateNotFoundException(String globalisationMessageCode, String defaultUserMessage, Object... defaultUserMessageArgs) {
        super(globalisationMessageCode, defaultUserMessage, defaultUserMessageArgs);
    }

    public static BusinessDateNotFoundException notExist(final String type, Throwable... e) {
        return new BusinessDateNotFoundException("error.msg.businessdate.type.not.exist",
                "Business date with type `" + type + "` does not exist.", type, e);
    }

    public static BusinessDateNotFoundException notFound(final String type, Throwable... e) {
        return new BusinessDateNotFoundException("error.msg.businessdate.not.found", "Business date with type `" + type + "` is not found.",
                type, e);
    }

}
