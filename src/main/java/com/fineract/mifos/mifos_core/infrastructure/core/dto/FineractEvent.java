package com.fineract.mifos.mifos_core.infrastructure.core.dto;

import com.fineract.mifos.mifos_core.infrastructure.core.domain.ContextHolder;
import org.springframework.context.ApplicationEvent;

import java.io.Serializable;

public abstract class FineractEvent extends ApplicationEvent implements ContextHolder, Serializable {

    private final FineractContext context;

    public FineractEvent(Object source, FineractContext context) {
        super(source);
        this.context = context;
    }

    @Override
    public FineractContext getContext() {
        return context;
    }

}
