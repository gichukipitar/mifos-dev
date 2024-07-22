package com.fineract.mifos.mifos_core.infrastructure.event.external.repository;

import com.fineract.mifos.mifos_core.infrastructure.event.external.entity.ExternalEventConfiguration;

public interface CustomExternalEventConfigurationRepository {

    ExternalEventConfiguration findExternalEventConfigurationByTypeWithNotFoundDetection(String externalEventType);
}
