package com.fineract.mifos.mifos_core.infrastructure.event.external.service;

import com.fineract.mifos.mifos_core.infrastructure.core.api.JsonCommand;
import com.fineract.mifos.mifos_core.infrastructure.core.data.CommandProcessingResult;
import com.fineract.mifos.mifos_core.infrastructure.core.data.CommandProcessingResultBuilder;
import com.fineract.mifos.mifos_core.infrastructure.event.external.command.ExternalEventConfigurationCommand;
import com.fineract.mifos.mifos_core.infrastructure.event.external.entity.ExternalEventConfiguration;
import com.fineract.mifos.mifos_core.infrastructure.event.external.repository.ExternalEventConfigurationRepository;
import com.fineract.mifos.mifos_core.infrastructure.event.external.serialization.ExternalEventConfigurationCommandFromApiJsonDeserializer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ExternalEventConfigurationWritePlatformServiceImpl implements ExternalEventConfigurationWritePlatformService {

    private final ExternalEventConfigurationRepository repository;
    private final ExternalEventConfigurationCommandFromApiJsonDeserializer fromApiJsonDeserializer;

    @Transactional
    @Override
    public CommandProcessingResult updateConfigurations(final JsonCommand command) {
        final ExternalEventConfigurationCommand configurationCommand = fromApiJsonDeserializer.commandFromApiJson(command.json());
        final Map<String, Boolean> commandConfigurations = configurationCommand.getExternalEventConfigurations();
        final Map<String, Object> changes = new HashMap<>();
        final Map<String, Boolean> changedConfigurations = new HashMap<>();
        final List<ExternalEventConfiguration> modifiedConfigurations = new ArrayList<>();

        for (Map.Entry<String, Boolean> entry : commandConfigurations.entrySet()) {
            final ExternalEventConfiguration configuration = repository
                    .findExternalEventConfigurationByTypeWithNotFoundDetection(entry.getKey());
            configuration.setEnabled(entry.getValue());
            changedConfigurations.put(entry.getKey(), entry.getValue());
            modifiedConfigurations.add(configuration);
        }

        if (!modifiedConfigurations.isEmpty()) {
            this.repository.saveAll(modifiedConfigurations);
        }

        if (!changedConfigurations.isEmpty()) {
            changes.put("externalEventConfigurations", changedConfigurations);
        }

        return new CommandProcessingResultBuilder().withCommandId(command.commandId()).with(changes).build();
    }
}
