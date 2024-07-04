package com.fineract.mifos.mifos_core.organisation.office.entity;

import com.fineract.mifos.mifos_core.infrastructure.core.entity.AbstractPersistableCustom;
import com.fineract.mifos.mifos_core.infrastructure.core.domain.ExternalId;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "m_office", uniqueConstraints = { @UniqueConstraint(columnNames = { "name" }, name = "name_org"),
        @UniqueConstraint(columnNames = { "external_id" }, name = "externalid_org") })

public class Office extends AbstractPersistableCustom implements Serializable {
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private List<Office> children = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Office parent;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "hierarchy", length = 50)
    private String hierarchy;

    @Column(name = "opening_date", nullable = false)
    private LocalDate openingDate;

    @Column(name = "external_id", length = 100, unique = true)
    private ExternalId externalId;

}
