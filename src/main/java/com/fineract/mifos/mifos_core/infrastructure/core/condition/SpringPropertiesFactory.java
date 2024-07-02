package com.fineract.mifos.mifos_core.infrastructure.core.condition;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class SpringPropertiesFactory {

    private SpringPropertiesFactory() {}

    public static <T> T get(Environment environment, Class<T> clazz) {
        ConfigurationProperties configurationProperties = clazz.getAnnotation(ConfigurationProperties.class);
        if (configurationProperties == null) {
            throw new IllegalArgumentException("Not a class with @ConfigurationProperties annotation");
        }
        String prefix = configurationProperties.prefix();
        String value = configurationProperties.value();
        if (isBlank(prefix) && isBlank(value)) {
            throw new IllegalArgumentException("@ConfigurationProperties is missing both prefix and value properties");
        }
        String propertyName = isNotBlank(prefix) ? prefix : value;
        return get(environment, propertyName, clazz);
    }

    public static <T> T get(Environment environment, String propertyName, Class<T> clazz) {
        return Binder.get(environment).bind(propertyName, clazz).orElseThrow(
                () -> new IllegalArgumentException("Couldn't bind " + clazz.getSimpleName() + " to the '" + propertyName + "' property"));
    }

}
