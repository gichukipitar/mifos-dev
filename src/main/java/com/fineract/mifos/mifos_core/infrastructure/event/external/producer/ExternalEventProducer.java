package com.fineract.mifos.mifos_core.infrastructure.event.external.producer;

import com.fineract.mifos.mifos_core.infrastructure.event.external.exception.AcknowledgementTimeoutException;

import java.util.List;
import java.util.Map;

public interface ExternalEventProducer {

    /**
     * Sends the created ExternalEvents
     *
     * @param partitions
     *            is a Map<Long, List<byte[]>> partitions, the key here the id of the aggregated root. The value is list
     *            of external events belong to the same key, serialized into byte array
     * @throws AcknowledgementTimeoutException
     */
    void sendEvents(Map<Long, List<byte[]>> partitions) throws AcknowledgementTimeoutException;
}
