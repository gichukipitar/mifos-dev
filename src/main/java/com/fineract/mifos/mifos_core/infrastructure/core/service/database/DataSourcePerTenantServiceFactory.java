package com.fineract.mifos.mifos_core.infrastructure.core.service.database;

import com.fineract.mifos.mifos_core.infrastructure.core.config.FineractProperties;
import com.fineract.mifos.mifos_core.infrastructure.core.dto.FineractPlatformTenantConnection;
import com.zaxxer.hikari.HikariConfig;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 *
 * Factory class to get data source service based on the details stored in {@link FineractPlatformTenantConnection}
 * variable
 *
 */
@Component
@Slf4j
public class DataSourcePerTenantServiceFactory {

    private final HikariConfig hikariConfig;
    private final FineractProperties fineractProperties;
    private final ApplicationContext context;
    private final DataSource tenantDataSource;
    private final HikariDataSourceFactory hikariDataSourceFactory;

    private final DatabasePasswordEncryptor databasePasswordEncryptor;

    public DataSourcePerTenantServiceFactory(@Qualifier("hikariTenantDataSource") DataSource tenantDataSource, HikariConfig hikariConfig,
                                             FineractProperties fineractProperties, ApplicationContext context, HikariDataSourceFactory hikariDataSourceFactory,
                                             DatabasePasswordEncryptor databasePasswordEncryptor) {
        this.hikariConfig = hikariConfig;
        this.fineractProperties = fineractProperties;
        this.context = context;
        this.tenantDataSource = tenantDataSource;
        this.hikariDataSourceFactory = hikariDataSourceFactory;
        this.databasePasswordEncryptor = databasePasswordEncryptor;
    }

    @SuppressFBWarnings(value = "SLF4J_SIGN_ONLY_FORMAT")
    public DataSource createNewDataSourceFor(final FineractPlatformTenantConnection tenantConnection) {
        if (!databasePasswordEncryptor.isMasterPasswordHashValid(tenantConnection.getMasterPasswordHash())) {
            throw new IllegalArgumentException(
                    "Invalid master password on tenant connection %d.".formatted(tenantConnection.getConnectionId()));
        }
        String protocol = toProtocol(tenantDataSource);
        // Default properties for Writing
        String schemaServer = tenantConnection.getSchemaServer();
        String schemaPort = tenantConnection.getSchemaServerPort();
        String schemaName = tenantConnection.getSchemaName();
        String schemaUsername = tenantConnection.getSchemaUsername();
        String schemaPassword = tenantConnection.getSchemaPassword();
        String schemaConnectionParameters = tenantConnection.getSchemaConnectionParameters();
        // Properties to ReadOnly case
        if (fineractProperties.getMode().isReadOnlyMode()) {
            schemaServer = StringUtils.defaultIfBlank(tenantConnection.getReadOnlySchemaServer(), schemaServer);
            schemaPort = StringUtils.defaultIfBlank(tenantConnection.getReadOnlySchemaServerPort(), schemaPort);
            schemaName = StringUtils.defaultIfBlank(tenantConnection.getReadOnlySchemaName(), schemaName);
            schemaUsername = StringUtils.defaultIfBlank(tenantConnection.getReadOnlySchemaUsername(), schemaUsername);
            schemaPassword = StringUtils.defaultIfBlank(tenantConnection.getReadOnlySchemaPassword(), schemaPassword);
            schemaConnectionParameters = StringUtils.defaultIfBlank(tenantConnection.getReadOnlySchemaConnectionParameters(),
                    schemaConnectionParameters);
        }
        String jdbcUrl = toJdbcUrl(protocol, schemaServer, schemaPort, schemaName, schemaConnectionParameters);
        log.debug("{}", jdbcUrl);

        HikariConfig config = new HikariConfig();
        config.setReadOnly(fineractProperties.getMode().isReadOnlyMode());
        config.setJdbcUrl(jdbcUrl);
        config.setPoolName(schemaName + "_pool");
        config.setUsername(schemaUsername);
        config.setPassword(databasePasswordEncryptor.decrypt(schemaPassword));
        config.setMinimumIdle(getMinPoolSize(tenantConnection));
        config.setMaximumPoolSize(getMaxPoolSize(tenantConnection));
        config.setValidationTimeout(tenantConnection.getValidationInterval());
        config.setDriverClassName(hikariConfig.getDriverClassName());
        config.setConnectionTestQuery(hikariConfig.getConnectionTestQuery());
        config.setAutoCommit(hikariConfig.isAutoCommit());

        // https://github.com/brettwooldridge/HikariCP/wiki/MBean-(JMX)-Monitoring-and-Management
        config.setRegisterMbeans(true);

        // https://github.com/brettwooldridge/HikariCP/wiki/MySQL-Configuration
        // These are the properties for each Tenant DB; the same configuration
        // is also in src/main/resources/META-INF/spring/hikariDataSource.xml
        // for the all Tenants DB -->
        config.setDataSourceProperties(hikariConfig.getDataSourceProperties());

        return hikariDataSourceFactory.create(config);
    }

    private int getMaxPoolSize(FineractPlatformTenantConnection tenantConnection) {
        FineractProperties.FineractConfigProperties configOverride = fineractProperties.getTenant().getConfig();
        if (configOverride.isMaxPoolSizeSet()) {
            int maxPoolSize = configOverride.getMaxPoolSize();
            log.info("Overriding tenant datasource maximum pool size configuration to {}", maxPoolSize);
            return maxPoolSize;
        } else {
            return tenantConnection.getMaxActive();
        }
    }

    private int getMinPoolSize(FineractPlatformTenantConnection tenantConnection) {
        FineractProperties.FineractConfigProperties configOverride = fineractProperties.getTenant().getConfig();
        if (configOverride.isMinPoolSizeSet()) {
            int minPoolSize = configOverride.getMinPoolSize();
            log.info("Overriding tenant datasource minimum pool size configuration to {}", minPoolSize);
            return minPoolSize;
        } else {
            return tenantConnection.getInitialSize();
        }
    }

}
