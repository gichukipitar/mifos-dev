package com.fineract.mifos.mifos_core.infrastructure.dataqueries.dto;

import lombok.Getter;

import java.io.Serializable;
import java.util.List;

/**
 * Immutable data object representing datatable data.
 */
public final class DatatableData implements Serializable {

    @SuppressWarnings("unused")
    private final String applicationTableName;
    @Getter
    @SuppressWarnings("unused")
    private final String registeredTableName;
    @SuppressWarnings("unused")
    private final String entitySubType;
    @SuppressWarnings("unused")
    private final List<ResultsetColumnHeaderData> columnHeaderData;

    public static DatatableData create(final String applicationTableName, final String registeredTableName, final String entitySubType,
                                       final List<ResultsetColumnHeaderData> columnHeaderData) {
        return new DatatableData(applicationTableName, registeredTableName, entitySubType, columnHeaderData);
    }

    private DatatableData(final String applicationTableName, final String registeredTableName, final String entitySubType,
                          final List<ResultsetColumnHeaderData> columnHeaderData) {
        this.applicationTableName = applicationTableName;
        this.registeredTableName = registeredTableName;
        this.entitySubType = entitySubType;
        this.columnHeaderData = columnHeaderData;

    }

    public boolean hasColumn(final String columnName) {

        for (ResultsetColumnHeaderData c : this.columnHeaderData) {

            if (c.getColumnName().equals(columnName)) {
                return true;
            }
        }

        return false;
    }

}

