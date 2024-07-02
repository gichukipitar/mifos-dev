package com.fineract.mifos.mifos_core.infrastructure.core.data;

/**
 * The actual value of the parameter (if any) as passed to API.
 */
public class ApiErrorMessageArg {

    private Object value;

    public static ApiErrorMessageArg from(final Object object) {
        return new ApiErrorMessageArg(object);
    }

    public ApiErrorMessageArg(final Object object) {
        this.value = object;
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(final Object value) {
        this.value = value;
    }

}
