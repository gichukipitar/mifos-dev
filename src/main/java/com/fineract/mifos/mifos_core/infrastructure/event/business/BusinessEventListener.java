package com.fineract.mifos.mifos_core.infrastructure.event.business;

import com.fineract.mifos.mifos_core.infrastructure.event.business.dto.BusinessEvent;

/**
 * The interface to be implemented by classes that want to be informed when a Business Event executes. example: on
 * completion of loan approval event need to block guarantor funds
 *
 */
public interface BusinessEventListener<T extends BusinessEvent<?>> {

    /**
     * Implement this method for notifications after executing Business Event
     */
    void onBusinessEvent(T event);

}

