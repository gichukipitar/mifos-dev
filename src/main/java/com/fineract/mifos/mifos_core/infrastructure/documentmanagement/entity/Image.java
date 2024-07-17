package com.fineract.mifos.mifos_core.infrastructure.documentmanagement.entity;

import com.fineract.mifos.mifos_core.infrastructure.core.entity.AbstractPersistableCustom;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "m_image")
public final class Image extends AbstractPersistableCustom {

    @Column(name = "location", length = 500)
    private String location;

    @Column(name = "storage_type_enum")
    private Integer storageType;

    public Image(final String location, final StorageType storageType) {
        this.location = location;
        this.storageType = storageType.getValue();
    }

    Image() {

    }

    public String getLocation() {
        return this.location;
    }

    public Integer getStorageType() {
        return this.storageType;
    }

    public void setLocation(final String location) {
        this.location = location;
    }

    public void setStorageType(final Integer storageType) {
        this.storageType = storageType;
    }

}
