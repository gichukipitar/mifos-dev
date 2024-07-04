package com.fineract.mifos.mifos_core.infrastructure.core.dto;

import com.fineract.mifos.mifos_core.infrastructure.businessdate.dto.BusinessDateType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActionContext {

    DEFAULT(0, "Default context", BusinessDateType.BUSINESS_DATE), COB(1, "Close of Business context", BusinessDateType.COB_DATE);

    private final int order;
    private final String description;
    private final BusinessDateType businessDateType;

}
