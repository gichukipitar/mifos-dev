package com.fineract.mifos.mifos_core.infrastructure.core.service.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

import static java.lang.String.format;

@Component
public class MySQLQueryService implements DatabaseQueryService {

    private final DatabaseTypeResolver databaseTypeResolver;

    @Autowired
    public MySQLQueryService(DatabaseTypeResolver databaseTypeResolver) {
        this.databaseTypeResolver = databaseTypeResolver;
    }

    @Override
    public boolean isSupported() {
        return databaseTypeResolver.isMySQL();
    }

    @Override
    public boolean isTablePresent(DataSource dataSource, String tableName) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        SqlRowSet rs = jdbcTemplate.queryForRowSet(format("SHOW TABLES LIKE '%s'", tableName));
        return rs.next();
    }

    @Override
    public SqlRowSet getTableColumns(DataSource dataSource, String tableName) {
        final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        final String sql = "SELECT c.COLUMN_NAME, c.IS_NULLABLE, c.DATA_TYPE, c.CHARACTER_MAXIMUM_LENGTH, c.COLUMN_KEY FROM INFORMATION_SCHEMA.COLUMNS c WHERE TABLE_SCHEMA = schema() AND TABLE_NAME = ? ORDER BY ORDINAL_POSITION";

        final SqlRowSet columnDefinitions = jdbcTemplate.queryForRowSet(sql, new Object[] { tableName }); // NOSONAR
        if (columnDefinitions.next()) {
            return columnDefinitions;
        } else {
            throw new IllegalArgumentException("Table " + tableName + " is not found");
        }
    }

    @Override
    public List<IndexDetail> getTableIndexes(DataSource dataSource, String tableName) {
        final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        final String sql = "SELECT i.INDEX_NAME FROM INFORMATION_SCHEMA.STATISTICS i WHERE TABLE_SCHEMA = schema() AND TABLE_NAME = ?";
        final SqlRowSet indexDefinitions = jdbcTemplate.queryForRowSet(sql, new Object[] { tableName }); // NOSONAR
        if (indexDefinitions.next()) {
            return DatabaseIndexMapper.getIndexDetails(indexDefinitions);
        } else {
            throw new IllegalArgumentException("Table " + tableName + " is not found");
        }
    }
}
