package com.fineract.mifos.mifos_core.infrastructure.dataqueries.dto;

import java.util.List;

public final class ResultsetRowData {

    private final List<Object> row;

    public static ResultsetRowData create(final List<Object> rowValues) {
        return new ResultsetRowData(rowValues);
    }

    private ResultsetRowData(final List<Object> rowValues) {
        this.row = rowValues;
    }

    public List<Object> getRow() {
        return this.row;
    }
}
