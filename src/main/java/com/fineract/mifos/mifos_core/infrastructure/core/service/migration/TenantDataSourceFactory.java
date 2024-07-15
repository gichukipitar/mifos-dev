package com.fineract.mifos.mifos_core.infrastructure.core.service.migration;

import com.fineract.mifos.mifos_core.infrastructure.core.dto.FineractPlatformTenant;
import com.fineract.mifos.mifos_core.infrastructure.core.dto.FineractPlatformTenantConnection;
import com.fineract.mifos.mifos_core.infrastructure.core.service.database.DatabasePasswordEncryptor;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

import static com.fineract.mifos.mifos_core.infrastructure.core.dto.FineractPlatformTenantConnection.toJdbcUrl;
import static com.fineract.mifos.mifos_core.infrastructure.core.dto.FineractPlatformTenantConnection.toProtocol;

@Component
public class TenantDataSourceFactory {

    private static final Logger LOG = LoggerFactory.getLogger(TenantDataSourceFactory.class);

    private final HikariDataSource tenantDataSource;

    private final DatabasePasswordEncryptor databasePasswordEncryptor;

    @Autowired
    public TenantDataSourceFactory(@Qualifier("hikariTenantDataSource") HikariDataSource tenantDataSource,
                                   DatabasePasswordEncryptor databasePasswordEncryptor) {
        this.tenantDataSource = tenantDataSource;
        this.databasePasswordEncryptor = databasePasswordEncryptor;
    }

    public DataSource create(FineractPlatformTenant tenant) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(tenantDataSource.getDriverClassName());
        dataSource.setDataSourceProperties(tenantDataSource.getDataSourceProperties());
        dataSource.setMinimumIdle(tenantDataSource.getMinimumIdle());
        dataSource.setMaximumPoolSize(tenantDataSource.getMaximumPoolSize());
        dataSource.setIdleTimeout(tenantDataSource.getIdleTimeout());
        dataSource.setConnectionTestQuery(tenantDataSource.getConnectionTestQuery());

        FineractPlatformTenantConnection tenantConnection = tenant.getConnection();
        if (!databasePasswordEncryptor.isMasterPasswordHashValid(tenantConnection.getMasterPasswordHash())) {
            throw new IllegalArgumentException("Invalid master password");
        }
        dataSource.setUsername(tenantConnection.getSchemaUsername());
        dataSource.setPassword(databasePasswordEncryptor.decrypt(tenantConnection.getSchemaPassword()));
        String protocol = toProtocol(tenantDataSource);
        String tenantJdbcUrl = toJdbcUrl(protocol, tenantConnection.getSchemaServer(), tenantConnection.getSchemaServerPort(),
                tenantConnection.getSchemaName(), tenantConnection.getSchemaConnectionParameters());
        LOG.debug("JDBC URL for tenant {} is {}", tenant.getTenantIdentifier(), tenantJdbcUrl);
        dataSource.setJdbcUrl(tenantJdbcUrl);
        return dataSource;
    }
}
