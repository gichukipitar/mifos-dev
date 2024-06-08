package com.fineract.mifos.mifos_core.infrastructure.core.domain;

import com.fineract.mifos.mifos_core.infrastructure.core.service.DateUtils;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.data.domain.Auditable;
import org.springframework.data.jpa.domain.AbstractAuditable;

import java.time.OffsetDateTime;
import java.util.Optional;

import static com.fineract.mifos.mifos_core.infrastructure.core.domain.AuditableFieldsConstants.*;

/**
 * A custom copy of {@link AbstractAuditable} to override the column names used on database. It also uses OffsetDateTime
 * for created and modified. The datetimes will be converted from tenant TZ to UTC before storing (automatically
 * happens) and converted from System TZ to tenant TZ after fetching from DB
 *
 * Abstract base class for auditable entities. Stores the audit values in persistent fields.
 */

@MappedSuperclass
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractAuditableWithUTCDateTimeCustom extends AbstractPersistableCustom
        implements Auditable<Long, Long, OffsetDateTime> {

    private static final long serialVersionUID = 141481953116476081L;
    @Column(name = CREATED_BY_DB_FIELD, nullable = false)
    private Long createdBy;

    @Column(name = CREATED_DATE_DB_FIELD, nullable = false)
     private OffsetDateTime createdDate;

    @Column(name = LAST_MODIFIED_BY_DB_FIELD, nullable = false)

    private Long lastModifiedBy;

    @Column(name = LAST_MODIFIED_DATE_DB_FIELD, nullable = false)

    private OffsetDateTime lastModifiedDate;

    @Override
    @NotNull
    public Optional<Long> getCreatedBy() {
        return Optional.ofNullable(this.createdBy);
    }

    @Override
    @NotNull
    public Optional<OffsetDateTime> getCreatedDate() {
        return Optional.ofNullable(createdDate);
    }

    @NotNull
    public OffsetDateTime getCreatedDateTime() {
        return getCreatedDate().orElseGet(DateUtils::getAuditOffsetDateTime);
    }

    @Override
    @NotNull
    public Optional<Long> getLastModifiedBy() {
        return Optional.ofNullable(this.lastModifiedBy);
    }

    @Override
    @NotNull
    public Optional<OffsetDateTime> getLastModifiedDate() {
        return Optional.ofNullable(lastModifiedDate);
    }

    @NotNull
    public OffsetDateTime getLastModifiedDateTime() {
        return getLastModifiedDate().orElseGet(DateUtils::getAuditOffsetDateTime);
    }

}
