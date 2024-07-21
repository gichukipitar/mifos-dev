package com.fineract.mifos.mifos_core.infrastructure.event.external.handler;

import com.fineract.mifos.mifos_core.commands.annotation.CommandType;
import com.fineract.mifos.mifos_core.commands.handler.NewCommandSourceHandler;
import com.fineract.mifos.mifos_core.infrastructure.core.api.JsonCommand;
import com.fineract.mifos.mifos_core.infrastructure.core.data.CommandProcessingResult;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@CommandType(entity = "EXTERNAL_EVENT_CONFIGURATION", action = "UPDATE")
public class ExternalEventConfigurationUpdateHandler implements NewCommandSourceHandler {

    private final ExternalEventConfigurationWritePlatformService writePlatformService;

    @Transactional
    @Override
    public CommandProcessingResult processCommand(@NotNull final JsonCommand command) {
        return writePlatformService.updateConfigurations(command);
    }
}
