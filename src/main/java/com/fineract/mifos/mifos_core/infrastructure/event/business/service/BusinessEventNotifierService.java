package com.fineract.mifos.mifos_core.infrastructure.event.business.service;

import com.fineract.mifos.mifos_core.infrastructure.event.business.BusinessEventListener;
import com.fineract.mifos.mifos_core.infrastructure.event.business.dto.BusinessEvent;

/**
 * Implemented class is responsible for notifying the business event to registered listeners.
 *
 */
public interface BusinessEventNotifierService {

    /**
     * Method should be called to notify listeners after Business event execution for any pre-processing of event
     */
    void notifyPreBusinessEvent(BusinessEvent<?> businessEvent);

    /**
     * Method should be called to notify listeners after Business event execution for any post-processing of event
     */
    void notifyPostBusinessEvent(BusinessEvent<?> businessEvent);

    /**
     * Method is to register a class as listener for pre-processing of any Business event
     */
    <T extends BusinessEvent<?>> void addPreBusinessEventListener(Class<T> eventType, BusinessEventListener<T> listener);

    /**
     * Method is to register a class as listener for post-processing of any Business event
     */
    <T extends BusinessEvent<?>> void addPostBusinessEventListener(Class<T> eventType, BusinessEventListener<T> listener);

    void startExternalEventRecording();

    void stopExternalEventRecording();

    void resetEventRecording();
}

