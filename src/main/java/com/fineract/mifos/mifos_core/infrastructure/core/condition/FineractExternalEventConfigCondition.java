package com.fineract.mifos.mifos_core.infrastructure.core.condition;

import com.fineract.mifos.mifos_core.infrastructure.core.config.FineractProperties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FineractExternalEventConfigCondition extends PropertiesCondition{

    @Override
    protected boolean matches(FineractProperties properties) {
        int partitionSize = properties.getEvents().getExternal().getPartitionSize();
        boolean conditionFails = false;
        if (partitionSize > 25000) {
            conditionFails = true;
            log.error("The partition size for external event partitions cannot be bigger than 25000.");
        } else if (partitionSize < 1) {
            conditionFails = true;
            log.error("The partition size for external event partitions must be positive.");
        }
        return conditionFails;
    }

}
