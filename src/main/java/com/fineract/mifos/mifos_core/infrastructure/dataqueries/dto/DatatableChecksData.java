package com.fineract.mifos.mifos_core.infrastructure.dataqueries.dto;

import java.io.Serializable;

public class DatatableChecksData implements Serializable {

    private static final long serialVersionUID = 3113568562509206452L;
    private final String entity;
    private final String dataTableName;

    public DatatableChecksData(final String entity, final String dataTableName) {
        this.entity = entity;
        this.dataTableName = dataTableName;
    }
}
