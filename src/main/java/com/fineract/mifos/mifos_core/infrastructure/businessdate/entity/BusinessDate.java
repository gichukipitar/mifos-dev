package com.fineract.mifos.mifos_core.infrastructure.businessdate.entity;

import com.fineract.mifos.mifos_core.infrastructure.businessdate.dto.BusinessDateType;
import com.fineract.mifos.mifos_core.infrastructure.core.entity.AbstractAuditableWithUTCDateTimeCustom;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "m_business_date", uniqueConstraints = { @UniqueConstraint(name = "uq_business_date_type", columnNames = { "type" }) })

public class BusinessDate extends AbstractAuditableWithUTCDateTimeCustom {
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private BusinessDateType type;

    @Column(name = "date", columnDefinition = "DATE")
    private LocalDate date;

    @Version
    private Long version;

    public static BusinessDate instance(@NotNull BusinessDateType businessDateType, @NotNull LocalDate date) {
        return new BusinessDate().setType(businessDateType).setDate(date);
    }
}
