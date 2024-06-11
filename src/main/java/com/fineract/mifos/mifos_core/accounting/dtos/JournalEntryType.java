package com.fineract.mifos.mifos_core.accounting.dtos;


public enum JournalEntryType {
    CREDIT(1, "journalEntryType.credit"), DEBIT(2, "journalEntrytType.debit");

    private final Integer value;
    private final String code;

    JournalEntryType(final Integer value, final String code) {
        this.value = value;
        this.code = code;
    }

    public Integer getValue() {
        return this.value;
    }

    public String getCode() {
        return this.code;
    }

    public static JournalEntryType fromInt(final Integer v) {
        if (v == null) {
            return null;
        }

        switch (v) {
            case 1:
                return CREDIT;
            case 2:
                return DEBIT;
            default:
                return null;
        }
    }
    @Override
    public String toString() {
        return name();
    }

    public boolean isDebitType() {
        return this.equals(DEBIT);
    }

    public boolean isCreditType() {
        return this.equals(CREDIT);
    }
}
