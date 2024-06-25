package com.fineract.mifos.mifos_core.infrastructure.configuration.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Immutable data object for global configuration.
 */

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class GlobalConfigurationData {


    @SuppressWarnings("unused")
    private List<GlobalConfigurationPropertyData> globalConfiguration;
}
