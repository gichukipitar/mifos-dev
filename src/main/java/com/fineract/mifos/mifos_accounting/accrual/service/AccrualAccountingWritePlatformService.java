package com.fineract.mifos.mifos_accounting.accrual.service;

import com.fineract.mifos.mifos_core.infrastructure.core.api.JsonCommand;
import com.fineract.mifos.mifos_core.infrastructure.core.data.CommandProcessingResult;
import org.springframework.stereotype.Service;

@Service
public interface AccrualAccountingWritePlatformService {
    CommandProcessingResult executeLoansPeriodicAccrual(JsonCommand command);

}
