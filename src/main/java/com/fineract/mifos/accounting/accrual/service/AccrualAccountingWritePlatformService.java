package com.fineract.mifos.accounting.accrual.service;

import com.fineract.mifos.core.infrastructure.core.api.JsonCommand;
import com.fineract.mifos.core.infrastructure.core.data.CommandProcessingResult;

public interface AccrualAccountingWritePlatformService {
    CommandProcessingResult executeLoansPeriodicAccrual(JsonCommand command);

}
