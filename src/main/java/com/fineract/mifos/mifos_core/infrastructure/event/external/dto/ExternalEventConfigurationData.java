package com.fineract.mifos.mifos_core.infrastructure.event.external.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ExternalEventConfigurationData {

    private List<ExternalEventConfigurationItemData> externalEventConfiguration;
}
