package com.fineract.mifos.mifos_core.infrastructure.core.config;

import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.ConditionContext;

public final class ExplicitConfigurationPropertiesFactory {

    private ExplicitConfigurationPropertiesFactory() {}

    public static <T> T getProperty(ConditionContext context, String propertyName, Class<T> type) {
        return Binder.get(context.getEnvironment()).bind(propertyName, type).orElse(null);
    }

}
