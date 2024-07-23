package com.fineract.mifos.mifos_core.infrastructure.event.external.service;

import com.fineract.mifos.mifos_core.infrastructure.core.dto.FineractPlatformTenant;
import com.fineract.mifos.mifos_core.infrastructure.core.service.migration.TenantDataSourceFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@RequiredArgsConstructor
@Component
public class JdbcTemplateFactory {

    private final TenantDataSourceFactory tenantDataSourceFactory;

    public JdbcTemplate create(FineractPlatformTenant tenant) {
        DataSource tenantDataSource = tenantDataSourceFactory.create(tenant);
        return new JdbcTemplate(tenantDataSource);
    }

    public NamedParameterJdbcTemplate createNamedParameterJdbcTemplate(FineractPlatformTenant tenant) {
        DataSource tenantDataSource = tenantDataSourceFactory.create(tenant);
        return new NamedParameterJdbcTemplate(tenantDataSource);
    }
}
