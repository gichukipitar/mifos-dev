package com.fineract.mifos.mifos_core.infrastructure.core.dto;

import com.fineract.mifos.mifos_core.infrastructure.core.data.ApiErrorMessageArg;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;

@Getter
public final class ApiParameterError {

    /**
     * A developer friendly plain English description of why the HTTP error response was returned from the API.
     */
    private final String developerMessage;

    /**
     * A user friendly plain English description of why the HTTP error response was returned from the API that can be
     * presented to end users.
     */
    private final String defaultUserMessage;

    /**
     * A code that can be used for globalisation support by client applications of the API.
     */
    private final String userMessageGlobalisationCode;

    /**
     * The name of the field or parameter passed to the API that this error relates to.
     */
    private String parameterName;

    /**
     * The actual value of the parameter (if any) as passed to API.
     */
    private final Object value;

    /**
     * Arguments related to the user error message.
     */
    private final List<ApiErrorMessageArg> args;

    public static ApiParameterError generalError(final String globalisationMessageCode, final String defaultUserMessage,
                                                 final Object... defaultUserMessageArgs) {
        return new ApiParameterError(globalisationMessageCode, defaultUserMessage, defaultUserMessageArgs, "id", null);
    }

    public static ApiParameterError resourceIdentifierNotFound(final String globalisationMessageCode, final String defaultUserMessage,
                                                               final Object... defaultUserMessageArgs) {
        return new ApiParameterError(globalisationMessageCode, defaultUserMessage, defaultUserMessageArgs, "id", null);
    }

    public static ApiParameterError parameterError(final String globalisationMessageCode, final String defaultUserMessage,
                                                   final String parameterName, final Object... defaultUserMessageArgs) {
        return new ApiParameterError(globalisationMessageCode, defaultUserMessage, defaultUserMessageArgs, parameterName, null);
    }

    public static ApiParameterError parameterErrorWithValue(final String globalisationMessageCode, final String defaultUserMessage,
                                                            final String parameterName, final String value, final Object... defaultUserMessageArgs) {
        return new ApiParameterError(globalisationMessageCode, defaultUserMessage, defaultUserMessageArgs, parameterName, value);
    }

    private ApiParameterError(final String globalisationMessageCode, final String defaultUserMessage, final Object[] defaultUserMessageArgs,
                              String parameterName, String value) {
        this.userMessageGlobalisationCode = globalisationMessageCode;
        this.developerMessage = defaultUserMessage;
        this.defaultUserMessage = defaultUserMessage;
        this.parameterName = parameterName;
        this.value = value;

        final List<ApiErrorMessageArg> messageArgs = new ArrayList<>();
        if (defaultUserMessageArgs != null) {
            for (final Object object : defaultUserMessageArgs) {
                if (object instanceof LocalDate) {
                    final DateTimeFormatter dateFormatter = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd").toFormatter();
                    final String formattedDate = dateFormatter.format((LocalDate) object);
                    messageArgs.add(ApiErrorMessageArg.from(formattedDate));
                } else {
                    messageArgs.add(ApiErrorMessageArg.from(object));
                }
            }
        }
        this.args = messageArgs;
    }

    public void setParameterName(final String parameterName) {
        this.parameterName = parameterName;
    }

    @Override
    public String toString() {
        return developerMessage;
    }
}
