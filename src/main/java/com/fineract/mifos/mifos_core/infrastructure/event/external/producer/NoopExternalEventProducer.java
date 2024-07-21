package com.fineract.mifos.mifos_core.infrastructure.event.external.producer;

import com.fineract.mifos.mifos_core.infrastructure.event.external.exception.AcknowledgementTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Conditional(NoopExternalEventEnabled.class)
@Slf4j
public class NoopExternalEventProducer implements ExternalEventProducer {

    @Override
    public void sendEvents(Map<Long, List<byte[]>> messages) throws AcknowledgementTimeoutException {}
}
