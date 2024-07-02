package com.fineract.mifos.mifos_core.infrastructure.core.condition;

import com.fineract.mifos.mifos_core.infrastructure.core.config.FineractProperties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FineractRemoteJobMessageHandlerCondition extends PropertiesCondition{

    @Override
    protected boolean matches(FineractProperties properties) {
        boolean isSpringEventsEnabled = properties.getRemoteJobMessageHandler().getSpringEvents().isEnabled();

        boolean conditionFails = false;
        if (isAnyMessageHandlerConfigured(properties) && isBatchInstance(properties)) {
            if (!isOnlyOneMessageHandlerEnabled(properties)) {
                conditionFails = true;
                log.error("For remote partitioning jobs exactly one Message Handler must be enabled.");
            } else if (isSpringEventsEnabled && !isBatchManagerAndWorkerTogether(properties)) {
                conditionFails = true;
                log.error("If Spring Event Message Handler is enabled, the instance must be Batch Manager and Batch Worker too.");
            }
        }
        return conditionFails;
    }

    private boolean isOnlyOneMessageHandlerEnabled(FineractProperties properties) {
        boolean isSpringEventsEnabled = properties.getRemoteJobMessageHandler().getSpringEvents().isEnabled();
        boolean isJmsEnabled = properties.getRemoteJobMessageHandler().getJms().isEnabled();
        return isSpringEventsEnabled ^ isJmsEnabled;
    }

    private boolean isAnyMessageHandlerConfigured(FineractProperties properties) {
        boolean isSpringEventsEnabled = properties.getRemoteJobMessageHandler().getSpringEvents().isEnabled();
        boolean isJmsEnabled = properties.getRemoteJobMessageHandler().getJms().isEnabled();
        return isSpringEventsEnabled || isJmsEnabled;
    }

    private boolean isBatchInstance(FineractProperties properties) {
        boolean isBatchManagerModeEnabled = properties.getMode().isBatchManagerEnabled();
        boolean isBatchWorkerModeEnabled = properties.getMode().isBatchWorkerEnabled();
        return isBatchManagerModeEnabled || isBatchWorkerModeEnabled;
    }

    private boolean isBatchManagerAndWorkerTogether(FineractProperties properties) {
        boolean isBatchManagerModeEnabled = properties.getMode().isBatchManagerEnabled();
        boolean isBatchWorkerModeEnabled = properties.getMode().isBatchWorkerEnabled();
        return isBatchManagerModeEnabled && isBatchWorkerModeEnabled;
    }

}
