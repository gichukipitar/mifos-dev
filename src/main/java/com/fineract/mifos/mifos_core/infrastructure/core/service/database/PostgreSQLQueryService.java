package com.fineract.mifos.mifos_core.infrastructure.core.service.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.Objects;

import static java.lang.String.format;

@Component
public class PostgreSQLQueryService implements DatabaseQueryService {

    private final DatabaseTypeResolver databaseTypeResolver;

    @Autowired
    public PostgreSQLQueryService(DatabaseTypeResolver databaseTypeResolver) {
        this.databaseTypeResolver = databaseTypeResolver;
    }

    @Override
    public boolean isSupported() {
        return databaseTypeResolver.isPostgreSQL();
    }

    @Override
    public boolean isTablePresent(DataSource dataSource, String tableName) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        Integer result = jdbcTemplate.queryForObject(format("SELECT COUNT(table_name) " + "FROM information_schema.tables "
                + "WHERE table_schema = 'public' " + "AND table_name = '%s';", tableName), Integer.class);
        return Objects.equals(result, 1);
    }

    @Override
    public SqlRowSet getTableColumns(DataSource dataSource, String tableName) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "SELECT column_name, is_nullable, data_type,"
                + " coalesce(character_maximum_length, numeric_precision, datetime_precision) AS max_length, ordinal_position = 1 AS column_key"
                + " FROM information_schema.columns WHERE table_catalog = current_catalog AND table_schema = current_schema AND table_name = '"
                + tableName + "' ORDER BY ordinal_position";
        final SqlRowSet columnDefinitions = jdbcTemplate.queryForRowSet(sql); // NOSONAR
        if (columnDefinitions.next()) {
            return columnDefinitions;
        } else {
            throw new IllegalArgumentException("Table " + tableName + " is not found");
        }
    }

    @Override
    public List<IndexDetail> getTableIndexes(DataSource dataSource, String tableName) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "SELECT indexname FROM pg_indexes WHERE schemaname = 'public' AND tablename = '" + tableName + "'";
        final SqlRowSet indexDefinitions = jdbcTemplate.queryForRowSet(sql); // NOSONAR
        if (indexDefinitions.next()) {
            return DatabaseIndexMapper.getIndexDetails(indexDefinitions);
        } else {
            throw new IllegalArgumentException("Table " + tableName + " is not found");
        }
    }
}
