package com.fineract.mifos.mifos_core.infrastructure.configuration.service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * Temporary (static) configuration service container.
 *
 * Provide static access to the configuration service TODO: To be deleted when the code cleanup / refactor finished (of
 * Loan.java) and we dont need this workaround anymore
 */
@Component
@RequiredArgsConstructor
public class TemporaryConfigurationServiceContainer implements InitializingBean {

    private static volatile ConfigurationDomainService STATIC_REF_CONFIGURATION_SERVICE;
    private final ConfigurationDomainService configurationDomainService;

    // To avoid any abuse of this temporary solution, only the `isExternalIdAutoGenerationEnabled()` is exposed
    public static boolean isExternalIdAutoGenerationEnabled() {
        return TemporaryConfigurationServiceContainer.STATIC_REF_CONFIGURATION_SERVICE.isExternalIdAutoGenerationEnabled();
    }

    public static String getAccrualDateConfigForCharge() {
        return TemporaryConfigurationServiceContainer.STATIC_REF_CONFIGURATION_SERVICE.getAccrualDateConfigForCharge();
    }

    @SuppressFBWarnings("ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD")
    @Override
    public void afterPropertiesSet() throws Exception {
        TemporaryConfigurationServiceContainer.STATIC_REF_CONFIGURATION_SERVICE = configurationDomainService;
    }

}
