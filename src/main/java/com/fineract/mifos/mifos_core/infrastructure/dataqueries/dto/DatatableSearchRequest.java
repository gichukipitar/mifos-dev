package com.fineract.mifos.mifos_core.infrastructure.dataqueries.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Immutable data object representing datatable data.
 */
@Data
@NoArgsConstructor
public final class DatatableSearchRequest implements Serializable {

    private List<ColumnFilter> columnFilters;

    private List<String> resultColumns;

    private String datatable;

}

