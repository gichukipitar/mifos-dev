package com.fineract.mifos.mifos_core.infrastructure.event.external.config;

import com.fineract.mifos.mifos_core.infrastructure.core.condition.PropertiesCondition;
import com.fineract.mifos.mifos_core.infrastructure.core.config.FineractProperties;
import org.apache.commons.lang3.StringUtils;

public class EnableExternalEventTopicCondition extends PropertiesCondition {

    @Override
    protected boolean matches(FineractProperties properties) {
        return StringUtils.isNotBlank(properties.getEvents().getExternal().getProducer().getJms().getEventTopicName());
    }
}
