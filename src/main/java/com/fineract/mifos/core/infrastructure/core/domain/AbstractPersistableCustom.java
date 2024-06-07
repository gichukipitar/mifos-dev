package com.fineract.mifos.core.infrastructure.core.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;

@Data
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
