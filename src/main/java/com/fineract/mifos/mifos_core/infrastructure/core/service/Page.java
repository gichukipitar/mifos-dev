package com.fineract.mifos.mifos_core.infrastructure.core.service;

import java.io.Serializable;
import java.util.List;

public class Page<E> implements Serializable {

    private final Integer totalFilteredRecords;
    private final List<E> pageItems;

    public Page(final List<E> pageItems, final Integer totalFilteredRecords) {
        this.pageItems = pageItems;
        this.totalFilteredRecords = totalFilteredRecords;
    }

    public int getTotalFilteredRecords() {
        return this.totalFilteredRecords;
    }

    public List<E> getPageItems() {
        return this.pageItems;
    }

}
