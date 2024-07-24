package com.fineract.mifos.mifos_core.infrastructure.event.external.service;

import com.fineract.mifos.mifos_core.infrastructure.core.service.DataEnricherProcessor;
import com.fineract.mifos.mifos_core.infrastructure.event.business.dto.BulkBusinessEvent;
import com.fineract.mifos.mifos_core.infrastructure.event.business.dto.BusinessEvent;
import com.fineract.mifos.mifos_core.infrastructure.event.external.entity.ExternalEvent;
import com.fineract.mifos.mifos_core.infrastructure.event.external.repository.ExternalEventRepository;
import com.fineract.mifos.mifos_core.infrastructure.event.external.service.idempotency.ExternalEventIdempotencyKeyGenerator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ExternalEventService {

    private final ExternalEventRepository repository;
    private final ExternalEventIdempotencyKeyGenerator idempotencyKeyGenerator;
    private final BusinessEventSerializerFactory serializerFactory;
    private final ByteBufferConverter byteBufferConverter;
    private final BulkMessageItemFactory bulkMessageItemFactory;
    private final DataEnricherProcessor dataEnricherProcessor;

    private EntityManager entityManager;

    public <T> void postEvent(BusinessEvent<T> event) {
        if (event == null) {
            throw new IllegalArgumentException("event cannot be null");
        }

        try {
            flushChangesBeforeSerialization();
            ExternalEvent externalEvent;
            if (event instanceof BulkBusinessEvent) {
                externalEvent = handleBulkBusinessEvent((BulkBusinessEvent) event);
            } else {
                externalEvent = handleRegularBusinessEvent(event);
            }
            repository.save(externalEvent);
            log.debug("Saved message with idempotency key: [{}] of type [{}] and category [{}]", externalEvent.getIdempotencyKey(),
                    externalEvent.getType(), externalEvent.getCategory());
        } catch (IOException e) {
            throw new RuntimeException("Error while serializing event " + event.getClass().getSimpleName(), e);
        }

    }

    private ExternalEvent handleBulkBusinessEvent(BulkBusinessEvent bulkBusinessEvent) throws IOException {
        List<BulkMessageItemV1> messages = new ArrayList<>();
        List<BusinessEvent<?>> events = bulkBusinessEvent.get();
        for (int i = 0; i < events.size(); i++) {
            BusinessEvent<?> event = events.get(i);
            int id = i + 1;
            BulkMessageItemV1 message = bulkMessageItemFactory.createBulkMessageItem(id, event);
            messages.add(message);
        }
        String idempotencyKey = idempotencyKeyGenerator.generate(bulkBusinessEvent);
        BulkMessagePayloadV1 avroDto = new BulkMessagePayloadV1(messages);
        byte[] data = byteBufferConverter.convert(avroDto.toByteBuffer());

        return new ExternalEvent(bulkBusinessEvent.getType(), bulkBusinessEvent.getCategory(), BulkMessagePayloadV1.class.getName(), data,
                idempotencyKey, bulkBusinessEvent.getAggregateRootId());
    }

    private <T> ExternalEvent handleRegularBusinessEvent(BusinessEvent<T> event) throws IOException {
        String eventType = event.getType();
        String eventCategory = event.getCategory();
        String idempotencyKey = idempotencyKeyGenerator.generate(event);
        BusinessEventSerializer serializer = serializerFactory.create(event);
        String schema = serializer.getSupportedSchema().getName();
        ByteBufferSerializable avroDto = dataEnricherProcessor.enrich(serializer.toAvroDTO(event));
        ByteBuffer buffer = avroDto.toByteBuffer();
        byte[] data = byteBufferConverter.convert(buffer);
        Long aggregateRootId = event.getAggregateRootId();

        return new ExternalEvent(eventType, eventCategory, schema, data, idempotencyKey, aggregateRootId);
    }

    private void flushChangesBeforeSerialization() {
        entityManager.flush();
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}