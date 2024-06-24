package com.fineract.mifos.mifos_core.infrastructure.codes.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Immutable data object represent code-value data in system.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class CodeValueData implements Serializable {

    private Long id;
    private String name;
    private Integer position;
    private String description;
    private boolean active;
    private boolean mandatory;

    public static CodeValueData instance(final Long id, final String name, final Integer position, final boolean isActive,
                                         final boolean mandatory) {
        String description = null;
        return new CodeValueData().setId(id).setName(name).setPosition(position).setDescription(description).setActive(isActive)
                .setMandatory(mandatory);
    }

    public static CodeValueData instance(final Long id, final String name, final String description, final boolean isActive,
                                         final boolean mandatory) {
        Integer position = null;
        return new CodeValueData().setId(id).setName(name).setPosition(position).setDescription(description).setActive(isActive)
                .setMandatory(mandatory);
    }

    public static CodeValueData instance(final Long id, final String name, final String description, final boolean isActive) {
        Integer position = null;
        boolean mandatory = false;

        return new CodeValueData().setId(id).setName(name).setPosition(position).setDescription(description).setActive(isActive)
                .setMandatory(mandatory);
    }

    public static CodeValueData instance(final Long id, final String name) {
        String description = null;
        Integer position = null;
        boolean isActive = false;
        boolean mandatory = false;

        return new CodeValueData().setId(id).setName(name).setPosition(position).setDescription(description).setActive(isActive)
                .setMandatory(mandatory);
    }

    public static CodeValueData instance(final Long id, final String name, final Integer position, final String description,
                                         final boolean isActive, final boolean mandatory) {
        return new CodeValueData().setId(id).setName(name).setPosition(position).setDescription(description).setActive(isActive)
                .setMandatory(mandatory);
    }


}
