package com.fineract.mifos.mifos_core.infrastructure.businessdate.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class BusinessDateData implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String description;
    private String type;
    private LocalDate date;

    public static BusinessDateData instance(BusinessDateType businessDateType, LocalDate value) {
        return new BusinessDateData().setType(businessDateType.getName()).setDate(value);
    }
}
