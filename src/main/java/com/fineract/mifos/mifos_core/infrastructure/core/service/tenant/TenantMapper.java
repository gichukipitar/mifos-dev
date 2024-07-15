package com.fineract.mifos.mifos_core.infrastructure.core.service.tenant;

import com.fineract.mifos.mifos_core.infrastructure.core.dto.FineractPlatformTenant;
import com.fineract.mifos.mifos_core.infrastructure.core.dto.FineractPlatformTenantConnection;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class TenantMapper implements RowMapper<FineractPlatformTenant> {

    private final boolean isReport;
    private static final String TENANT_SERVER_CONNECTION_BUILDER = " t.id, ts.id as connectionId , "
            + " t.timezone_id as timezoneId , t.name,t.identifier, ts.schema_name as schemaName, ts.schema_server as schemaServer,"
            + " ts.schema_server_port as schemaServerPort, ts.schema_connection_parameters as schemaConnectionParameters, ts.auto_update as autoUpdate,"
            + " ts.schema_username as schemaUsername, ts.schema_password as schemaPassword , ts.pool_initial_size as initialSize,"
            + " ts.pool_validation_interval as validationInterval, ts.pool_remove_abandoned as removeAbandoned, ts.pool_remove_abandoned_timeout as removeAbandonedTimeout,"
            + " ts.pool_log_abandoned as logAbandoned, ts.pool_abandon_when_percentage_full as abandonedWhenPercentageFull, ts.pool_test_on_borrow as testOnBorrow,"
            + " ts.pool_max_active as poolMaxActive, ts.pool_min_idle as poolMinIdle, ts.pool_max_idle as poolMaxIdle,"
            + " ts.pool_suspect_timeout as poolSuspectTimeout, ts.pool_time_between_eviction_runs_millis as poolTimeBetweenEvictionRunsMillis,"
            + " ts.pool_min_evictable_idle_time_millis as poolMinEvictableIdleTimeMillis,"
            + " ts.readonly_schema_server as readOnlySchemaServer, " + " ts.readonly_schema_server_port as readOnlySchemaServerPort, "
            + " ts.readonly_schema_name as readOnlySchemaName, " + " ts.readonly_schema_username as readOnlySchemaUsername, "
            + " ts.readonly_schema_password as readOnlySchemaPassword, "
            + " ts.readonly_schema_connection_parameters as readOnlySchemaConnectionParameters, "
            + " ts.master_password_hash as masterPasswordHash " + " from tenants t left join tenant_server_connections ts ";
    private final StringBuilder sqlBuilder = new StringBuilder(TENANT_SERVER_CONNECTION_BUILDER);

    public TenantMapper(boolean isReport) {
        this.isReport = isReport;
    }

    public String schema() {
        if (this.isReport) {
            this.sqlBuilder.append(" on t.report_Id = ts.id");
        } else {
            this.sqlBuilder.append(" on t.oltp_Id = ts.id");
        }
        return this.sqlBuilder.toString();
    }

    @Override
    public FineractPlatformTenant mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {
        final Long id = rs.getLong("id");
        final String tenantIdentifier = rs.getString("identifier");
        final String name = rs.getString("name");
        final String timezoneId = rs.getString("timezoneId");
        final FineractPlatformTenantConnection connection = getDBConnection(rs);
        return new FineractPlatformTenant(id, tenantIdentifier, name, timezoneId, connection);
    }

    // gets the DB connection
    private FineractPlatformTenantConnection getDBConnection(ResultSet rs) throws SQLException {

        final Long connectionId = rs.getLong("connectionId");
        final String schemaName = rs.getString("schemaName");
        final String schemaServer = rs.getString("schemaServer");
        final String schemaServerPort = rs.getString("schemaServerPort");
        final String schemaConnectionParameters = rs.getString("schemaConnectionParameters");
        final String schemaUsername = rs.getString("schemaUsername");
        final String schemaPassword = rs.getString("schemaPassword");
        final String readOnlySchemaName = rs.getString("readOnlySchemaName");
        final String readOnlySchemaServer = rs.getString("readOnlySchemaServer");
        final String readOnlySchemaServerPort = rs.getString("readOnlySchemaServerPort");
        final String readOnlySchemaUsername = rs.getString("readOnlySchemaUsername");
        final String readOnlySchemaPassword = rs.getString("readOnlySchemaPassword");
        final String readOnlySchemaConnectionParameters = rs.getString("readOnlySchemaConnectionParameters");

        final boolean autoUpdateEnabled = rs.getBoolean("autoUpdate");
        final int initialSize = rs.getInt("initialSize");
        final boolean testOnBorrow = rs.getBoolean("testOnBorrow");
        final long validationInterval = rs.getLong("validationInterval");
        final boolean removeAbandoned = rs.getBoolean("removeAbandoned");
        final int removeAbandonedTimeout = rs.getInt("removeAbandonedTimeout");
        final boolean logAbandoned = rs.getBoolean("logAbandoned");
        final int abandonWhenPercentageFull = rs.getInt("abandonedWhenPercentageFull");
        final int maxActive = rs.getInt("poolMaxActive");
        final int minIdle = rs.getInt("poolMinIdle");
        final int maxIdle = rs.getInt("poolMaxIdle");
        final int suspectTimeout = rs.getInt("poolSuspectTimeout");
        final int timeBetweenEvictionRunsMillis = rs.getInt("poolTimeBetweenEvictionRunsMillis");
        final int minEvictableIdleTimeMillis = rs.getInt("poolMinEvictableIdleTimeMillis");
        final String masterPasswordHash = rs.getString("masterPasswordHash");

        return new FineractPlatformTenantConnection(connectionId, schemaName, schemaServer, schemaServerPort, schemaConnectionParameters,
                schemaUsername, schemaPassword, autoUpdateEnabled, initialSize, validationInterval, removeAbandoned, removeAbandonedTimeout,
                logAbandoned, abandonWhenPercentageFull, maxActive, minIdle, maxIdle, suspectTimeout, timeBetweenEvictionRunsMillis,
                minEvictableIdleTimeMillis, testOnBorrow, readOnlySchemaServer, readOnlySchemaServerPort, readOnlySchemaName,
                readOnlySchemaUsername, readOnlySchemaPassword, readOnlySchemaConnectionParameters, masterPasswordHash);
    }
}
