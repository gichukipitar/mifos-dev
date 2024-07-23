package com.fineract.mifos.mifos_core.infrastructure.event.external.service;

import com.fineract.mifos.mifos_core.infrastructure.core.api.JsonCommand;
import com.fineract.mifos.mifos_core.infrastructure.core.data.CommandProcessingResult;

public interface ExternalEventConfigurationWritePlatformService {

    CommandProcessingResult updateConfigurations(JsonCommand command);
}
