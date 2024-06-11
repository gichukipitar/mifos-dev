package com.fineract.mifos.mifos_core.accounting.service;

import com.fineract.mifos.mifos_core.infrastructure.core.dto.EnumOptionData;
import lombok.Data;
import lombok.Getter;

@Data
public enum GLAccountType {
    ASSET(1, "accountType.asset"), LIABILITY(2, "accountType.liability"), EQUITY(3, "accountType.equity"), INCOME(4,
            "accountType.income"), EXPENSE(5, "accountType.expense");
    @Getter
    private final Integer value;
    @Getter
    private final String code;

    GLAccountType(Integer value, String code) {
        this.value = value;
        this.code = code;
    }

    @Getter
    private static int minValue;
    @Getter
    private static int maxValue;
    static {
        int i = 0;
        for (final GLAccountType type : GLAccountType.values()) {
            if (i == 0) {
                minValue = type.value;
            }
            if (minValue >= type.value) {
                minValue = type.value;
            }
            if (maxValue < type.value) {
                maxValue = type.value;
            }
            i = i + 1;
        }
    }

    public static EnumOptionData fromString(String accountType) {
        Long accountTypeId = null;
        if (accountType != null && accountType.equalsIgnoreCase(ASSET.toString())) {
            accountTypeId = 1L;
            return new EnumOptionData(accountTypeId, null, null);
        } else if (accountType != null && accountType.equalsIgnoreCase(LIABILITY.toString())) {
            accountTypeId = 2L;
            return new EnumOptionData(accountTypeId, null, null);
        } else if (accountType != null && accountType.equalsIgnoreCase(EQUITY.toString())) {
            accountTypeId = 3L;
            return new EnumOptionData(accountTypeId, null, null);
        } else if (accountType != null && accountType.equalsIgnoreCase(INCOME.toString())) {
            accountTypeId = 4L;
            return new EnumOptionData(accountTypeId, null, null);
        } else if (accountType != null && accountType.equalsIgnoreCase(EXPENSE.toString())) {
            accountTypeId = 5L;
            return new EnumOptionData(accountTypeId, null, null);
        } else {
            return null;
        }
    }

    public static GLAccountType fromInt(final Integer v) {
        if (v == null) {
            return null;
        }

        switch (v) {
            case 1:
                return ASSET;
            case 2:
                return LIABILITY;
            case 3:
                return EQUITY;
            case 4:
                return INCOME;
            case 5:
                return EXPENSE;
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        return name().toString();
    }

    public boolean isAssetType() {
        return this.value.equals(GLAccountType.ASSET.getValue());
    }

    public boolean isLiabilityType() {
        return this.value.equals(GLAccountType.LIABILITY.getValue());
    }

    public boolean isEquityType() {
        return this.value.equals(GLAccountType.EQUITY.getValue());
    }

    public boolean isIncomeType() {
        return this.value.equals(GLAccountType.INCOME.getValue());
    }

    public boolean isExpenseType() {
        return this.value.equals(GLAccountType.EXPENSE.getValue());
    }

}
