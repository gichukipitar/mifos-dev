package com.fineract.mifos.mifos_core.accounting.dtos;


import com.fineract.mifos.mifos_core.accounting.service.GLAccountType;
import com.fineract.mifos.mifos_core.accounting.utils.AccountingEnumerations;
import com.fineract.mifos.mifos_core.infrastructure.core.dto.EnumOptionData;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import com.fineract.mifos.mifos_core.accounting.service.GLAccountUsage;

/**
 * Immutable object representing a General Ledger Account
 *
 * Note: no getter/setters required as google-gson will produce json from fields of object.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class GLAccountData implements Serializable {

    private Long id;
    private String name;
    private Long parentId;
    private String glCode;
    private Boolean disabled;
    private Boolean manualEntriesAllowed;
    private EnumOptionData type;
    private EnumOptionData usage;
    private String description;
    private String nameDecorated;
    private CodeValueData tagId;
    private Long organizationRunningBalance;

    // templates
    private List<EnumOptionData> accountTypeOptions;
    private List<EnumOptionData> usageOptions;
    private List<GLAccountData> assetHeaderAccountOptions;
    private List<GLAccountData> liabilityHeaderAccountOptions;
    private List<GLAccountData> equityHeaderAccountOptions;
    private List<GLAccountData> incomeHeaderAccountOptions;
    private List<GLAccountData> expenseHeaderAccountOptions;
    private Collection<CodeValueData> allowedAssetsTagOptions;
    private Collection<CodeValueData> allowedLiabilitiesTagOptions;
    private Collection<CodeValueData> allowedEquityTagOptions;
    private Collection<CodeValueData> allowedIncomeTagOptions;
    private Collection<CodeValueData> allowedExpensesTagOptions;

    // import fields
    private transient Integer rowIndex;

    public static GLAccountData importInstance(String name, Long parentId, String glCode, Boolean manualEntriesAllowed, EnumOptionData type,
                                               EnumOptionData usage, String description, CodeValueData tagId, Integer rowIndex) {
        return new GLAccountData().setName(name).setParentId(parentId).setGlCode(glCode).setManualEntriesAllowed(manualEntriesAllowed)
                .setType(type).setUsage(usage).setDescription(description).setTagId(tagId).setRowIndex(rowIndex);
    }

    public static GLAccountData createFrom(final Long id) {

        return new GLAccountData().setId(id);
    }

    public static GLAccountData sensibleDefaultsForNewGLAccountCreation(final Integer glAccType) {
        final boolean disabled = false;
        final boolean manualEntriesAllowed = true;
        final EnumOptionData type;
        if (glAccType != null && glAccType >= GLAccountType.getMinValue() && glAccType <= GLAccountType.getMaxValue()) {
            type = AccountingEnumerations.gLAccountType(glAccType);
        } else {
            type = AccountingEnumerations.gLAccountType(GLAccountType.ASSET);
        }
        final EnumOptionData usage = AccountingEnumerations.gLAccountUsage(GLAccountUsage.DETAIL);

        return new GLAccountData().setDisabled(disabled).setManualEntriesAllowed(manualEntriesAllowed).setType(type).setUsage(usage);
    }


}
