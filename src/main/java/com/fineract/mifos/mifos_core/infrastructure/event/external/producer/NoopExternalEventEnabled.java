package com.fineract.mifos.mifos_core.infrastructure.event.external.producer;

import com.fineract.mifos.mifos_core.infrastructure.core.condition.PropertiesCondition;
import com.fineract.mifos.mifos_core.infrastructure.core.config.FineractProperties;

public class NoopExternalEventEnabled extends PropertiesCondition {

    @Override
    protected boolean matches(FineractProperties properties) {
        return (!getEffectiveJMSProperty(properties) && !getEffectiveKafkaProperty(properties));
    }

    private boolean getEffectiveJMSProperty(FineractProperties properties) {
        if (properties.getEvents() == null || properties.getEvents().getExternal() == null
                || properties.getEvents().getExternal().getProducer() == null
                || properties.getEvents().getExternal().getProducer().getJms() == null) {
            return false;
        }
        return properties.getEvents().getExternal().getProducer().getJms().isEnabled();
    }

    private boolean getEffectiveKafkaProperty(FineractProperties properties) {
        if (properties.getEvents() == null || properties.getEvents().getExternal() == null
                || properties.getEvents().getExternal().getProducer() == null
                || properties.getEvents().getExternal().getProducer().getKafka() == null) {
            return false;
        }
        return properties.getEvents().getExternal().getProducer().getKafka().isEnabled();
    }

}
