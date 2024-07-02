package com.fineract.mifos.mifos_core.infrastructure.core.condition;

import com.fineract.mifos.mifos_core.infrastructure.core.config.FineractProperties;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public abstract class PropertiesCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        FineractProperties properties = SpringPropertiesFactory.get(context.getEnvironment(), FineractProperties.class);
        return matches(properties);
    }

    protected abstract boolean matches(FineractProperties properties);

}
