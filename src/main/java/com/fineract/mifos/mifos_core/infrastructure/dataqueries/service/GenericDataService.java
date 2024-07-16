package com.fineract.mifos.mifos_core.infrastructure.dataqueries.service;

import com.fineract.mifos.mifos_core.infrastructure.dataqueries.dto.GenericResultsetData;
import com.fineract.mifos.mifos_core.infrastructure.dataqueries.dto.ResultsetColumnHeaderData;
import com.fineract.mifos.mifos_core.infrastructure.dataqueries.dto.ResultsetRowData;

import java.util.List;

public interface GenericDataService {

    GenericResultsetData fillGenericResultSet(String sql);

    List<ResultsetColumnHeaderData> fillResultsetColumnHeaders(String tableName);

    List<ResultsetRowData> fillResultsetRowData(String sql, List<ResultsetColumnHeaderData> columnHeaders);

    String generateJsonFromGenericResultsetData(GenericResultsetData grs);

    String replace(String str, String pattern, String replace);

    String wrapSQL(String sql);

    boolean isExplicitlyUnique(String tableName, String columnName);

    boolean isExplicitlyIndexed(String tableName, String columnName);
}
