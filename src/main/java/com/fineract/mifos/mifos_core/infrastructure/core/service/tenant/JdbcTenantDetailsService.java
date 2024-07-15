package com.fineract.mifos.mifos_core.infrastructure.core.service.tenant;

import com.fineract.mifos.mifos_core.infrastructure.core.dto.FineractPlatformTenant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * A JDBC implementation of {@link TenantDetailsService} for loading a tenants details by a
 * <code>tenantIdentifier</code>.
 */
@Service
public class JdbcTenantDetailsService implements TenantDetailsService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcTenantDetailsService(@Qualifier("hikariTenantDataSource") final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    @Cacheable(value = "tenantsById")
    public FineractPlatformTenant loadTenantById(final String tenantIdentifier) {
        if (isBlank(tenantIdentifier)) {
            throw new IllegalArgumentException("tenantIdentifier cannot be blank");
        }
        try {
            final TenantMapper rm = new TenantMapper(false);
            final String sql = "select " + rm.schema() + " where t.identifier = ?";

            return this.jdbcTemplate.queryForObject(sql, rm, new Object[] { tenantIdentifier }); // NOSONAR
        } catch (final EmptyResultDataAccessException e) {
            throw new InvalidTenantIdentifierException("The tenant identifier: " + tenantIdentifier + " is not valid.", e);
        }
    }

    @Override
    public List<FineractPlatformTenant> findAllTenants() {
        final TenantMapper rm = new TenantMapper(false);
        final String sql = "select  " + rm.schema();

        final List<FineractPlatformTenant> fineractPlatformTenants = this.jdbcTemplate.query(sql, rm); // NOSONAR
        return fineractPlatformTenants;
    }
}

