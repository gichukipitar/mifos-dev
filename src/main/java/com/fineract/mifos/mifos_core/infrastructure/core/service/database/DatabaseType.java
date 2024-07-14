package com.fineract.mifos.mifos_core.infrastructure.core.service.database;

public enum DatabaseType {

    MYSQL, //
    POSTGRESQL, //
    ;

    public boolean isMySql() {
        return this == MYSQL;
    }

    public boolean isPostgres() {
        return this == POSTGRESQL;
    }
}