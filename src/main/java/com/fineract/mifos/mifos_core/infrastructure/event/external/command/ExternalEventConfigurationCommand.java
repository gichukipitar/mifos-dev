package com.fineract.mifos.mifos_core.infrastructure.event.external.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
@Getter
public class ExternalEventConfigurationCommand {

    private final Map<String, Boolean> externalEventConfigurations;
}
