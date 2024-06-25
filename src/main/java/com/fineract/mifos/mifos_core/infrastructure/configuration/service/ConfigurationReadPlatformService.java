package com.fineract.mifos.mifos_core.infrastructure.configuration.service;

import com.fineract.mifos.mifos_core.infrastructure.configuration.dto.GlobalConfigurationData;
import com.fineract.mifos.mifos_core.infrastructure.configuration.dto.GlobalConfigurationPropertyData;

public interface ConfigurationReadPlatformService {

    GlobalConfigurationPropertyData retrieveGlobalConfiguration(Long configId);

    GlobalConfigurationPropertyData retrieveGlobalConfiguration(String name);

    GlobalConfigurationData retrieveGlobalConfiguration(boolean survey);

}
