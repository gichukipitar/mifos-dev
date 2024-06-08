package com.fineract.mifos.mifos_core.infrastructure.core.exception;

import java.util.ArrayList;
import java.util.List;

public class AbstractPlatformException extends RuntimeException {
    private static final Object[] NO_ARGS = new Object[0];

    private final String globalisationMessageCode;
    private final String defaultUserMessage;
    private final Object[] defaultUserMessageArgs;

    protected AbstractPlatformException(String globalisationMessageCode, String defaultUserMessage) {
        super(defaultUserMessage);
        this.globalisationMessageCode = globalisationMessageCode;
        this.defaultUserMessage = defaultUserMessage;
        this.defaultUserMessageArgs = NO_ARGS;
    }

    protected AbstractPlatformException(String globalisationMessageCode, String defaultUserMessage, Throwable cause) {
        super(defaultUserMessage, cause);
        this.globalisationMessageCode = globalisationMessageCode;
        this.defaultUserMessage = defaultUserMessage;
        this.defaultUserMessageArgs = NO_ARGS;
    }

    protected AbstractPlatformException(String globalisationMessageCode, String defaultUserMessage, Object[] defaultUserMessageArgs) {
        super(defaultUserMessage, findThrowableCause(defaultUserMessageArgs));
        this.globalisationMessageCode = globalisationMessageCode;
        this.defaultUserMessage = defaultUserMessage;
        this.defaultUserMessageArgs = AbstractPlatformException.filterThrowableCause(defaultUserMessageArgs);
    }

    private static Throwable findThrowableCause(Object[] defaultUserMessageArgs) {
        if (defaultUserMessageArgs == null) {
            return null;
        }
        for (Object defaultUserMessageArg : defaultUserMessageArgs) {
            if (defaultUserMessageArg instanceof Throwable) {
                return (Throwable) defaultUserMessageArg;
            }
        }
        return null;
    }

    private static Object[] filterThrowableCause(Object[] defaultUserMessageArgs) {
        if (defaultUserMessageArgs == null) {
            return NO_ARGS;
        }
        List<Object> filteredDefaultUserMessageArgs = new ArrayList<>(defaultUserMessageArgs.length);
        for (Object defaultUserMessageArg : defaultUserMessageArgs) {
            if (!(defaultUserMessageArg instanceof Throwable)) {
                filteredDefaultUserMessageArgs.add(defaultUserMessageArg);
            }
        }
        return filteredDefaultUserMessageArgs.toArray();
    }

    public final String getGlobalisationMessageCode() {
        return this.globalisationMessageCode;
    }

    public final String getDefaultUserMessage() {
        return this.defaultUserMessage;
    }

    public final Object[] getDefaultUserMessageArgs() {
        return this.defaultUserMessageArgs;
    }
}