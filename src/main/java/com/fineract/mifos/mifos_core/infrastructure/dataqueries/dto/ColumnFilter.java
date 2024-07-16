package com.fineract.mifos.mifos_core.infrastructure.dataqueries.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Immutable data object representing datatable data.
 */
@Data
@NoArgsConstructor
public final class ColumnFilter implements Serializable {

    private String columnName;

    private String columnValue;

    private String columnOperation;

}

