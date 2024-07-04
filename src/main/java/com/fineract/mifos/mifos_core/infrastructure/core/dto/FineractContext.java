package com.fineract.mifos.mifos_core.infrastructure.core.dto;

import com.fineract.mifos.mifos_core.infrastructure.businessdate.dto.BusinessDateType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;

@AllArgsConstructor
@Jacksonized
@Builder
@Getter
public class FineractContext implements Serializable {

    private final String contextHolder;
    private final FineractPlatformTenant tenantContext;
    private final String authTokenContext;
    private final HashMap<BusinessDateType, LocalDate> businessDateContext;
    private final ActionContext actionContext;

}
