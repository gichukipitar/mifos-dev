package com.fineract.mifos.mifos_core.infrastructure.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class EnumOptionData implements Serializable {
    private final Long id;
    private final String code;
    private final String value;
}
