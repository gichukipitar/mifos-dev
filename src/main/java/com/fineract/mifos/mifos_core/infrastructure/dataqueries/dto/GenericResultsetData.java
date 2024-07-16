package com.fineract.mifos.mifos_core.infrastructure.dataqueries.dto;

import com.fineract.mifos.mifos_core.infrastructure.core.service.database.JdbcJavaType;

import java.util.List;

/**
 * Immutable data object for generic resultset data.
 */
public final class GenericResultsetData {

    private final List<ResultsetColumnHeaderData> columnHeaders;
    private final List<ResultsetRowData> data;

    public GenericResultsetData(final List<ResultsetColumnHeaderData> columnHeaders, final List<ResultsetRowData> resultsetDataRows) {
        this.columnHeaders = columnHeaders;
        this.data = resultsetDataRows;
    }

    public List<ResultsetColumnHeaderData> getColumnHeaders() {
        return this.columnHeaders;
    }

    public List<ResultsetRowData> getData() {
        return this.data;
    }

    public JdbcJavaType getColTypeOfColumnNamed(final String columnName) {
        for (final ResultsetColumnHeaderData columnHeader : this.columnHeaders) {
            if (columnHeader.isNamed(columnName)) {
                return columnHeader.getColumnType();
            }
        }
        return null;
    }

    public boolean hasNoEntries() {
        return this.data.isEmpty();
    }

    public boolean hasEntries() {
        return !hasNoEntries();
    }

    public boolean hasMoreThanOneEntry() {
        return this.data.size() > 1;
    }
}

