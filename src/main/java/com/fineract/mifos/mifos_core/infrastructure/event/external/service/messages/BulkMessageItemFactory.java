package com.fineract.mifos.mifos_core.infrastructure.event.external.service.messages;

import com.fineract.mifos.mifos_core.infrastructure.core.service.DataEnricherProcessor;
import com.fineract.mifos.mifos_core.infrastructure.event.business.dto.BusinessEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.ByteBuffer;

@Component
@RequiredArgsConstructor
public class BulkMessageItemFactory {

    private final BusinessEventSerializerFactory serializerFactory;
    private final ByteBufferConverter byteBufferConverter;
    private final DataEnricherProcessor dataEnricherProcessor;

    public BulkMessageItemV1 createBulkMessageItem(int id, BusinessEvent<?> event) throws IOException {
        BusinessEventSerializer eventSerializer = serializerFactory.create(event);
        ByteBufferSerializable avroDto = dataEnricherProcessor.enrich(eventSerializer.toAvroDTO(event));
        ByteBuffer buffer = avroDto.toByteBuffer();
        byte[] serializedContent = byteBufferConverter.convert(buffer);
        String type = event.getType();
        String category = "nocategory"; // TODO: switch this to the actual category when implemented
        String schema = eventSerializer.getSupportedSchema().getName();
        ByteBuffer data = byteBufferConverter.convert(serializedContent);
        return new BulkMessageItemV1(id, type, category, schema, data);
    }
}
