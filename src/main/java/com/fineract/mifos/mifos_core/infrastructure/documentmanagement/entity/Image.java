package com.fineract.mifos.mifos_core.infrastructure.documentmanagement.entity;

import com.fineract.mifos.mifos_core.infrastructure.core.entity.AbstractPersistableCustom;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "m_image")
public class Image extends AbstractPersistableCustom {

    @Column(name = "location", length = 500)
    private String location;

    @Column(name = "storage_type_enum")
    private Integer storageType;

    public Image(final String location, final StorageType storageType) {
        this.location = location;
        this.storageType = storageType.getValue();
    }

    public Image() {

    }

}
