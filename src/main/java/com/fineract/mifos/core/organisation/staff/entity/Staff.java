package com.fineract.mifos.core.organisation.staff.entity;

import com.fineract.mifos.core.infrastructure.core.domain.AbstractPersistableCustom;
import com.fineract.mifos.core.organisation.office.entity.Office;
import jakarta.persistence.*;
import lombok.Data;

import java.awt.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "m_staff", uniqueConstraints = { @UniqueConstraint(columnNames = { "display_name" }, name = "display_name"),
        @UniqueConstraint(columnNames = { "external_id" }, name = "external_id_UNIQUE"),
        @UniqueConstraint(columnNames = { "mobile_no" }, name = "mobile_no_UNIQUE") })

public class Staff extends AbstractPersistableCustom {
    @Column(name = "firstname", length = 50)
    private String firstname;

    @Column(name = "lastname", length = 50)
    private String lastname;

    @Column(name = "display_name", length = 100)
    private String displayName;

    @Column(name = "mobile_no", length = 50, nullable = false, unique = true)
    private String mobileNo;

    @Column(name = "external_id", length = 100, unique = true)
    private String externalId;

    @Column(name = "email_address", length = 50, unique = true)
    private String emailAddress;

    @ManyToOne
    @JoinColumn(name = "office_id", nullable = false)
    private Office office;

    @Column(name = "is_loan_officer", nullable = false)
    private boolean loanOfficer;

    @Column(name = "organisational_role_enum")
    private Integer organisationalRoleType;

    @Column(name = "is_active", nullable = false)
    private boolean active;

    @Column(name = "joining_date")
    private LocalDate joiningDate;

    @ManyToOne
    @JoinColumn(name = "organisational_role_parent_staff_id")
    private Staff organisationalRoleParentStaff;

    @OneToOne(optional = true)
    @JoinColumn(name = "image_id")
    private Image image;





}
