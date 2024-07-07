package com.fineract.mifos.mifos_core.infrastructure.core.exception;

import lombok.Getter;

public abstract class AbstractIdempotentCommandException extends AbstractPlatformException{
    public static final String IDEMPOTENT_CACHE_HEADER = "x-served-from-cache";
    @Getter
    private final String action;

    @Getter
    private final String entity;
    @Getter
    private final String idempotencyKey;
    @Getter
    private final String response;

    protected AbstractIdempotentCommandException(String action, String entity, String idempotencyKey, String response) {
        super(null, null);
        this.action = action;
        this.entity = entity;
        this.idempotencyKey = idempotencyKey;
        this.response = response;
    }
}
