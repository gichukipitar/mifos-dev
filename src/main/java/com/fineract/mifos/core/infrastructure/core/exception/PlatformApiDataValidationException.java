package com.fineract.mifos.core.infrastructure.core.exception;

import com.fineract.mifos.core.infrastructure.core.dto.ApiParameterError;

import java.util.List;

public class PlatformApiDataValidationException extends AbstractPlatformException{
    private final List<ApiParameterError> errors;

    /**
     * Constructor. Consider simply using {@link DataValidatorBuilder#throwValidationErrors()} directly.
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

    public List<ApiParameterError> getErrors() {
        return this.errors;
    }

    @Override
    public String toString() {
        return "PlatformApiDataValidationException{" + "errors=" + errors + '}';
    }
}

