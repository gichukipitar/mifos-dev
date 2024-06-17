package com.fineract.mifos.mifos_core.infrastructure.businessdate.service;

import com.fineract.mifos.mifos_core.infrastructure.businessdate.dto.BusinessDateData;
import com.fineract.mifos.mifos_core.infrastructure.businessdate.dto.BusinessDateType;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Component
public interface BusinessDateReadPlatformService {
    List<BusinessDateData> findAll();

    BusinessDateData findByType(String type);

    HashMap<BusinessDateType, LocalDate> getBusinessDates();
}
