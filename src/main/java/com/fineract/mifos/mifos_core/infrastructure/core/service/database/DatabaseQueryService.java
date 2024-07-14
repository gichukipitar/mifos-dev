package com.fineract.mifos.mifos_core.infrastructure.core.service.database;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.util.List;

public interface DatabaseQueryService {

    boolean isSupported();

    boolean isTablePresent(DataSource dataSource, String tableName);

    // TODO: This needs to be improved to have a custom POJO return type instead of the raw SqlRowSet
    SqlRowSet getTableColumns(DataSource dataSource, String tableName);

    List<IndexDetail> getTableIndexes(DataSource dataSource, String tableName);
}
