package com.fineract.mifos.mifos_core.infrastructure.core.service.database;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.ArrayList;
import java.util.List;

public final class DatabaseIndexMapper {

    private DatabaseIndexMapper() {}

    public static List<IndexDetail> getIndexDetails(SqlRowSet rowset) {
        List<IndexDetail> indexes = new ArrayList<>();
        rowset.beforeFirst();
        while (rowset.next()) {
            indexes.add(new IndexDetail(rowset.getString(1)));
        }
        return indexes;
    }
}
