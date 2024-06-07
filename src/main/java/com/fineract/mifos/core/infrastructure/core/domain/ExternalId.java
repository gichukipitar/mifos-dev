package com.fineract.mifos.core.infrastructure.core.domain;

import com.fineract.mifos.core.infrastructure.core.exception.PlatformInternalServerException;
import io.micrometer.common.util.StringUtils;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Data
public class ExternalId implements Serializable {
    @Serial
    private static final long serialVersionUID = 1;
    private static final ExternalId empty = new ExternalId();
    private final String value;

    private ExternalId() {
        this.value = null;
    }

    public ExternalId(String value) {
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("error.external.id.cannot.be.blank");
        }
        this.value = value;
    }

    /**
     * @return Create a new ExternalId object where value is a newly generated UUID
     */
    public static ExternalId generate() {
        return new ExternalId(UUID.randomUUID().toString());
    }

    /**
     * @return Create and return an empty ExternalId object
     */
    public static ExternalId empty() {
        return empty;
    }

    /**
     * @return whether value is null for the ExternalId object (return true if value is null)
     */
    public boolean isEmpty() {
        return value == null;
    }

    /**
     * Throws exception if value was not set (value is null) for this object
     *
     * @throws PlatformInternalServerException
     *             if value was not set (value is null) for this object
     */
    public void throwExceptionIfEmpty() {
        if (isEmpty()) {
            throw new PlatformInternalServerException("error.external.id.is.not.set", "Internal state violation: External id is not set");
        }
    }
}
