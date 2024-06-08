package com.fineract.mifos.mifos_accounting.closure.dto;

import com.fineract.mifos.mifos_core.organisation.office.dto.OfficeData;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
/**
 * Immutable object representing a General Ledger Account
 *
 * Note: no getter/setters required as google-gson will produce json from fields of object.
 */
@Data
public class GLClosureData {
    private final Long id;
    private final Long officeId;
    private final String officeName;
    private final LocalDate closingDate;
    private final boolean deleted;
    private final LocalDate createdDate;
    private final LocalDate lastUpdatedDate;
    private final Long createdByUserId;
    private final String createdByUsername;
    private final Long lastUpdatedByUserId;
    private final String lastUpdatedByUsername;
    private final String comments;

    private Collection<OfficeData> allowedOffices = new ArrayList<>();
}
