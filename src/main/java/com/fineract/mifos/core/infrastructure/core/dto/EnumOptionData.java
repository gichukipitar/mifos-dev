package com.fineract.mifos.core.infrastructure.core.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class EnumOptionData implements Serializable {
    private final Long id;
    private final String code;
    private final String value;
}
