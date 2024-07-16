package com.fineract.mifos.mifos_core.infrastructure.dataqueries.dto;

import java.io.Serializable;

public class DatatableCheckStatusData implements Serializable {

    private final String name;
    private final int code;

    public DatatableCheckStatusData(final String name, final int code) {
        this.name = name;
        this.code = code;
    }

}
