package com.fineract.mifos.mifos_core.accounting.dtos;

import lombok.Data;

@Data
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
}
