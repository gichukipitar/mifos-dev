package com.fineract.mifos.mifos_core.useradministration.entity;

import com.fineract.mifos.mifos_core.infrastructure.core.domain.AbstractPersistableCustom;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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

    private Set<Permission> permissions = new HashSet<>();
    public Collection<Permission> getPermissions() {
        return this.permissions;
    }

}
