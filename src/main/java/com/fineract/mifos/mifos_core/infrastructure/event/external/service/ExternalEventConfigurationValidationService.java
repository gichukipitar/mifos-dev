package com.fineract.mifos.mifos_core.infrastructure.event.external.service;

import com.fineract.mifos.mifos_core.infrastructure.core.dto.FineractPlatformTenant;
import com.fineract.mifos.mifos_core.infrastructure.core.service.tenant.TenantDetailsService;
import com.fineract.mifos.mifos_core.infrastructure.event.business.dto.BulkBusinessEvent;
import com.fineract.mifos.mifos_core.infrastructure.event.business.dto.BusinessEvent;
import com.fineract.mifos.mifos_core.infrastructure.event.business.dto.NoExternalEvent;
import com.fineract.mifos.mifos_core.infrastructure.event.external.exception.ExternalEventConfigurationNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.boot.archive.scan.spi.ScanResult;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExternalEventConfigurationValidationService implements InitializingBean {

    private static final String EXTERNAL_EVENT_BUSINESS_INTERFACE = BusinessEvent.class.getName();
    private static final String BULK_BUSINESS_EVENT = BulkBusinessEvent.class.getName();
    private final TenantDetailsService tenantDetailsService;
    private final JdbcTemplateFactory jdbcTemplateFactory;
    private final ExternalEventSourceService externalEventSourceService;

    @Override
    public void afterPropertiesSet() throws Exception {
        validateEventConfigurationForAllTenants();
    }

    private void validateEventConfigurationForAllTenants() throws ExternalEventConfigurationNotFoundException {
        List<String> eventClasses = getAllEventClasses();
        List<FineractPlatformTenant> tenants = tenantDetailsService.findAllTenants();

        if (isNotEmpty(tenants)) {
            for (FineractPlatformTenant tenant : tenants) {
                validateEventConfigurationForIndividualTenant(tenant, eventClasses);
            }
        }
    }

    private void validateEventConfigurationForIndividualTenant(FineractPlatformTenant tenant, List<String> eventClasses)
            throws ExternalEventConfigurationNotFoundException {
        log.info("Validating external event configuration for {}", tenant.getTenantIdentifier());
        List<String> eventConfigurations = getExternalEventConfigurationsForTenant(tenant);
        if (log.isDebugEnabled()) {
            log.debug("Missing from eventClasses: {}", CollectionUtils.subtract(eventClasses, eventConfigurations));
            log.debug("Missing from eventConfigurations: {}", CollectionUtils.subtract(eventConfigurations, eventClasses));
        }

        if (eventClasses.size() != eventConfigurations.size()) {
            throw new ExternalEventConfigurationNotFoundException();
        }

        for (String eventTypeClass : eventClasses) {
            if (!eventConfigurations.contains(eventTypeClass)) {
                throw new ExternalEventConfigurationNotFoundException(eventTypeClass);
            }
        }
    }

    private List<String> getExternalEventConfigurationsForTenant(FineractPlatformTenant tenant) {
        final JdbcTemplate jdbcTemplate = jdbcTemplateFactory.create(tenant);
        final StringBuilder eventConfigurations = new StringBuilder();
        eventConfigurations.append("select me.type as type from m_external_event_configuration me");
        List<String> configuredEventTypes = jdbcTemplate.queryForList(eventConfigurations.toString(), String.class);
        return configuredEventTypes;
    }

    private List<String> getAllEventClasses() {
        List<String> sourcePackages = externalEventSourceService.getSourcePackages();
        if (log.isDebugEnabled()) {
            log.debug("Packages {}", sourcePackages);
        }
        String[] sourcePackagesForScan = new String[sourcePackages.size()];
        try (ScanResult scanResult = new ClassGraph().enableAllInfo().acceptPackages(sourcePackages.toArray(sourcePackagesForScan))
                .scan()) {
            ClassInfoList businessEventClasses = scanResult.getClassesImplementing(EXTERNAL_EVENT_BUSINESS_INTERFACE)
                    .filter(classInfo -> (!classInfo.implementsInterface(NoExternalEvent.class) && !classInfo.isInterface()
                            && !classInfo.isAbstract() && !classInfo.getName().equalsIgnoreCase(BULK_BUSINESS_EVENT)));
            return businessEventClasses.stream().map(classInfo -> classInfo.getSimpleName()).collect(Collectors.toList());
        }
    }
}
