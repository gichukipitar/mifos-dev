package com.fineract.mifos.mifos_core.infrastructure.event.external.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExternalEventConfigurationItemData {

    private String type;
    private boolean enabled;
}
