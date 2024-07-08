package com.fineract.mifos.mifos_core.infrastructure.core.exception;

import com.fineract.mifos.mifos_core.infrastructure.core.dto.ApiParameterError;
import lombok.Getter;

import java.util.List;

@Getter
public class PlatformApiDataValidationException extends AbstractPlatformException{
    private final List<ApiParameterError> errors;

    /**
     * Constructor. Consider simply using {@link #()} directly.
     *
     * @param errors
     *            list of {@link ApiParameterError} to throw
     */
    public PlatformApiDataValidationException(List<ApiParameterError> errors) {
        super("validation.msg.validation.errors.exist", "Validation errors exist.");
        this.errors = errors;
    }

    public PlatformApiDataValidationException(final List<ApiParameterError> errors, Throwable cause) {
        super("validation.msg.validation.errors.exist", "Validation errors exist.", cause);
        this.errors = errors;
    }

    public PlatformApiDataValidationException(String globalisationMessageCode, String defaultUserMessage, List<ApiParameterError> errors) {
        super(globalisationMessageCode, defaultUserMessage);
        this.errors = errors;
    }

    public PlatformApiDataValidationException(String globalisationMessageCode, String defaultUserMessage, List<ApiParameterError> errors,
                                              Throwable cause) {
        super(globalisationMessageCode, defaultUserMessage, cause);
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "PlatformApiDataValidationException{" + "errors=" + errors + '}';
    }
}

