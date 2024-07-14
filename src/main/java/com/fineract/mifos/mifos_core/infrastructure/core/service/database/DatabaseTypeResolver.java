package com.fineract.mifos.mifos_core.infrastructure.core.service.database;

import com.zaxxer.hikari.HikariConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class DatabaseTypeResolver implements InitializingBean {

    private static final Map<String, DatabaseType> DRIVER_MAPPING = Map.of("org.mariadb.jdbc.Driver", DatabaseType.MYSQL,
            "com.mysql.jdbc.Driver", DatabaseType.MYSQL, "com.mysql.cj.jdbc.Driver", DatabaseType.MYSQL, "org.postgresql.Driver",
            DatabaseType.POSTGRESQL);

    private static final AtomicReference<DatabaseType> currentDatabaseType = new AtomicReference<>();
    private final HikariConfig hikariConfig;

    @Autowired
    public DatabaseTypeResolver(HikariConfig hikariConfig) {
        this.hikariConfig = hikariConfig;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        currentDatabaseType.set(determineDatabaseType(hikariConfig.getDriverClassName()));
    }

    private DatabaseType determineDatabaseType(String driverClassName) {
        DatabaseType databaseType = DRIVER_MAPPING.get(driverClassName);
        if (databaseType == null) {
            throw new IllegalArgumentException("The driver's class is not supported " + driverClassName);
        }
        return databaseType;
    }

    public DatabaseType databaseType() {
        return currentDatabaseType.get();
    }

    public boolean isPostgreSQL() {
        return DatabaseType.POSTGRESQL == currentDatabaseType.get();
    }

    public boolean isMySQL() {
        return DatabaseType.MYSQL == currentDatabaseType.get();
    }
}
