package com.fineract.mifos.mifos_core.infrastructure.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;

/**
 * Make sure to modify the same class in the modules (fineract-investor, etc)
 *
 * Abstract base class for entities.
 *
 * Inspired by {@link org.springframework.data.jpa.domain.AbstractPersistable}, but Id is always Long (and this class
 * thus does not require generic parameterization), and auto-generation is of strategy
 * {@link jakarta.persistence.GenerationType#IDENTITY}.
 *
 * The {@link #equals(Object)} and {@link #hashCode()} methods are NOT implemented here, which is untypical for JPA
 * (it's usually implemented based on the Id), because "we end up with issues on OpenJPA" (TODO clarify this).
 */

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public class AbstractPersistableCustom implements Persistable<Long>, Serializable {
    private static final long serialVersionUID = 9181640245194392646L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Transient
    private boolean isNew = true;
    @PrePersist
    @PostLoad
    void markNotNew() {
        this.isNew = false;
    }

}
