package com.fineract.mifos.mifos_core.infrastructure.core.service.database;

import javax.sql.DataSource;

/**
 * A service for getting hold of the appropriate {@link DataSource} connection pool.
 */
public interface RoutingDataSourceService {

    DataSource retrieveDataSource();
}

