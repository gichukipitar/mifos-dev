package com.fineract.mifos.mifos_core.infrastructure.configuration.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * Immutable data object for global configuration property.
 */

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class GlobalConfigurationPropertyData {

    @SuppressWarnings("unused")
    private String name;
    @SuppressWarnings("unused")
    private boolean enabled;
    @SuppressWarnings("unused")
    private Long value;
    @SuppressWarnings("unused")
    private LocalDate dateValue;
    private String stringValue;
    @SuppressWarnings("unused")
    private Long id;
    @SuppressWarnings("unused")
    private String description;
    @SuppressWarnings("unused")
    private boolean trapDoor;

}
