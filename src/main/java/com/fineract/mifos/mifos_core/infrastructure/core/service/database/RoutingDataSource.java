package com.fineract.mifos.mifos_core.infrastructure.core.service.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Based on springs {@link AbstractRoutingDataSource} idea, this is a {@link DataSource} that routes or delegates to
 * another data source depending on the tenant details passed in the request.
 *
 * The tenant details are process earlier and stored in a {@link ThreadLocal}.
 *
 * The {@link RoutingDataSourceService} is responsible for returning the appropriate {@link DataSource} for the tenant
 * of this request.
 */
@Service(value = "dataSource")
@Primary
public class RoutingDataSource extends AbstractDataSource {

    @Autowired
    private RoutingDataSourceServiceFactory dataSourceServiceFactory;

    @Override
    public Connection getConnection() throws SQLException {
        return determineTargetDataSource().getConnection();
    }

    public DataSource determineTargetDataSource() {
        return this.dataSourceServiceFactory.determineDataSourceService().retrieveDataSource();
    }

    @Override
    public Connection getConnection(final String username, final String password) throws SQLException {
        return determineTargetDataSource().getConnection(username, password);
    }
}

