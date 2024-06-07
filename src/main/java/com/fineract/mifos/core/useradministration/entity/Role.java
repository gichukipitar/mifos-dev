package com.fineract.mifos.core.useradministration.entity;

import com.fineract.mifos.core.infrastructure.core.domain.AbstractPersistableCustom;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "m_role", uniqueConstraints = { @UniqueConstraint(columnNames = { "name" }, name = "unq_name") })

public class Role extends AbstractPersistableCustom implements Serializable {
    @Column(name = "name", unique = true, nullable = false, length = 100)
    private String name;

    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @Column(name = "is_disabled", nullable = false)
    private Boolean disabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "m_role_permission", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "permission_id"))

}
