package com.fineract.mifos.mifos_core.accounting.service;

import lombok.Data;

@Data
public enum PortfolioProductType {
    LOAN(1, "productType.loan"), SAVING(2, "productType.saving"), CLIENT(5, "productType.client"), PROVISIONING(3,
            "productType.provisioning"), SHARES(4, "productType.shares");

    private final Integer value;
    private final String code;

    PortfolioProductType(final Integer value, final String code) {
        this.value = value;
        this.code = code;
    }

    @Override
    public String toString() {
        return name().toString().replaceAll("_", " ");
    }

    public Integer getValue() {
        return this.value;
    }

    public String getCode() {
        return this.code;
    }

    public static PortfolioProductType fromInt(final Integer v) {
        if (v == null) {
            return null;
        }

        switch (v) {
            case 1:
                return LOAN;
            case 2:
                return SAVING;
            case 3:
                return CLIENT;
            case 4:
                return PROVISIONING;
            case 5:
                return SHARES;
            default:
                return null;
        }
    }

    public boolean isSavingProduct() {
        return this.equals(SAVING);
    }

    public boolean isLoanProduct() {
        return this.equals(LOAN);
    }

    public boolean isClient() {
        return this.equals(CLIENT);
    }

    public boolean isShareProduct() {
        return this.equals(SHARES);
    }

}
