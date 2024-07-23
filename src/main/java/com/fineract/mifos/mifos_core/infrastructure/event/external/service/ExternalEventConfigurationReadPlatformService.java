package com.fineract.mifos.mifos_core.infrastructure.event.external.service;

import com.fineract.mifos.mifos_core.infrastructure.event.external.dto.ExternalEventConfigurationData;
import org.springframework.stereotype.Component;

@Component
public interface ExternalEventConfigurationReadPlatformService {

    ExternalEventConfigurationData findAllExternalEventConfigurations();
}
