package com.fineract.mifos.mifos_core.infrastructure.businessdate.service;

import com.fineract.mifos.mifos_core.infrastructure.businessdate.dto.BusinessDateData;
import com.fineract.mifos.mifos_core.infrastructure.core.api.JsonCommand;
import com.fineract.mifos.mifos_core.infrastructure.core.data.CommandProcessingResult;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.stereotype.Component;

import java.util.Map;
@Component
public interface BusinessDateWritePlatformService {
    CommandProcessingResult updateBusinessDate(JsonCommand command);

    void adjustDate(BusinessDateData data, Map<String, Object> changes);

    void increaseCOBDateByOneDay() throws JobExecutionException;

    void increaseBusinessDateByOneDay() throws JobExecutionException;
}
