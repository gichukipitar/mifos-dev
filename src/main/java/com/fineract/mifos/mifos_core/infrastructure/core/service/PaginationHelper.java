package com.fineract.mifos.mifos_core.infrastructure.core.service;

import com.fineract.mifos.mifos_core.infrastructure.core.service.database.DatabaseSpecificSQLGenerator;
import com.fineract.mifos.mifos_core.infrastructure.core.service.database.DatabaseTypeResolver;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaginationHelper {

    private final DatabaseSpecificSQLGenerator sqlGenerator;
    private final DatabaseTypeResolver databaseTypeResolver;

    @Autowired
    public PaginationHelper(DatabaseSpecificSQLGenerator sqlGenerator, DatabaseTypeResolver databaseTypeResolver) {
        this.sqlGenerator = sqlGenerator;
        this.databaseTypeResolver = databaseTypeResolver;
    }

    public <E> Page<E> fetchPage(final JdbcTemplate jt, final String sqlFetchRows, final Object[] args, final RowMapper<E> rowMapper) {

        final List<E> items = jt.query(sqlFetchRows, rowMapper, args); // NOSONAR

        // determine how many rows are available
        final String sqlCountRows = sqlGenerator.countLastExecutedQueryResult(sqlFetchRows);
        final Integer totalFilteredRecords;
        if (databaseTypeResolver.isMySQL()) {
            totalFilteredRecords = jt.queryForObject(sqlCountRows, Integer.class); // NOSONAR
        } else {
            totalFilteredRecords = jt.queryForObject(sqlCountRows, Integer.class, args); // NOSONAR
        }

        return new Page<>(items, totalFilteredRecords);
    }

    public <E> Page<Long> fetchPage(JdbcTemplate jdbcTemplate, String sql, Class<Long> type) {
        final List<Long> items = jdbcTemplate.queryForList(sql, type);

        // determine how many rows are available
        String sqlCountRows = sqlGenerator.countLastExecutedQueryResult(sql);
        Integer totalFilteredRecords = jdbcTemplate.queryForObject(sqlCountRows, Integer.class);

        return new Page<>(items, ObjectUtils.defaultIfNull(totalFilteredRecords, 0));
    }
}
