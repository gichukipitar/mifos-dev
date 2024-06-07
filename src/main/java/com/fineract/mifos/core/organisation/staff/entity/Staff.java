package com.fineract.mifos.core.organisation.staff.entity;

import com.fineract.mifos.core.infrastructure.core.domain.AbstractPersistableCustom;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Data
@Entity
@Table(name = "m_staff", uniqueConstraints = { @UniqueConstraint(columnNames = { "display_name" }, name = "display_name"),
        @UniqueConstraint(columnNames = { "external_id" }, name = "external_id_UNIQUE"),
        @UniqueConstraint(columnNames = { "mobile_no" }, name = "mobile_no_UNIQUE") })

public class Staff extends AbstractPersistableCustom {

}
