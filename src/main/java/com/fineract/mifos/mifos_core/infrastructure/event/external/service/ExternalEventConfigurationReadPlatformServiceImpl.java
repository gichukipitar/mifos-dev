package com.fineract.mifos.mifos_core.infrastructure.event.external.service;

import com.fineract.mifos.mifos_core.infrastructure.event.external.dto.ExternalEventConfigurationData;
import com.fineract.mifos.mifos_core.infrastructure.event.external.entity.ExternalEventConfiguration;
import com.fineract.mifos.mifos_core.infrastructure.event.external.repository.ExternalEventConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExternalEventConfigurationReadPlatformServiceImpl implements ExternalEventConfigurationReadPlatformService {

    private final ExternalEventConfigurationRepository repository;
    private final ExternalEventsConfigurationMapper mapper;

    @Override
    public ExternalEventConfigurationData findAllExternalEventConfigurations() {
        ExternalEventConfigurationData configurationData = new ExternalEventConfigurationData();
        List<ExternalEventConfiguration> eventConfigurations = repository.findAll();
        configurationData.setExternalEventConfiguration(mapper.map(eventConfigurations));
        return configurationData;
    }

}
