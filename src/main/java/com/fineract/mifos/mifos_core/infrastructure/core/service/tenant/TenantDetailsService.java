package com.fineract.mifos.mifos_core.infrastructure.core.service.tenant;

import com.fineract.mifos.mifos_core.infrastructure.core.dto.FineractPlatformTenant;

import java.util.List;

public interface TenantDetailsService {

    FineractPlatformTenant loadTenantById(String tenantId);

    List<FineractPlatformTenant> findAllTenants();
}
