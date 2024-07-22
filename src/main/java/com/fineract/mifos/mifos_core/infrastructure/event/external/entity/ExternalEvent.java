package com.fineract.mifos.mifos_core.infrastructure.event.external.entity;

import com.fineract.mifos.mifos_core.infrastructure.core.entity.AbstractPersistableCustom;
import com.fineract.mifos.mifos_core.infrastructure.core.service.DateUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "m_external_event")
@Getter
@NoArgsConstructor
public class ExternalEvent extends AbstractPersistableCustom {

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "schema", nullable = false)
    private String schema;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "data", nullable = false)
    private byte[] data;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Setter
    private ExternalEventStatus status;

    @Column(name = "sent_at", nullable = true)
    @Setter
    private OffsetDateTime sentAt;

    @Column(name = "idempotency_key", nullable = false)
    private String idempotencyKey;

    @Column(name = "business_date", nullable = false)
    private LocalDate businessDate;

    @Column(name = "aggregate_root_id", nullable = true)
    private Long aggregateRootId;

    public ExternalEvent(String type, String category, String schema, byte[] data, String idempotencyKey, Long aggregateRootId) {
        this.type = type;
        this.category = category;
        this.schema = schema;
        this.data = data;
        this.idempotencyKey = idempotencyKey;
        this.aggregateRootId = aggregateRootId;
        this.createdAt = DateUtils.getAuditOffsetDateTime();
        this.status = ExternalEventStatus.TO_BE_SENT;
        this.businessDate = DateUtils.getBusinessLocalDate();
    }
}
