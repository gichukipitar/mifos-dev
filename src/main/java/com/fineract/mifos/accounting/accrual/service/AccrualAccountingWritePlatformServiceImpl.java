package com.fineract.mifos.accounting.accrual.service;

import com.fineract.mifos.core.infrastructure.core.api.JsonCommand;
import com.fineract.mifos.core.infrastructure.core.data.CommandProcessingResult;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class AccrualAccountingWritePlatformServiceImpl implements AccrualAccountingWritePlatformService {
    private final LoanAccrualPlatformService loanAccrualPlatformService;
    private final AccrualAccountingDataValidator accountingDataValidator;
    @Override
    public CommandProcessingResult executeLoansPeriodicAccrual(JsonCommand command) {
        this.accountingDataValidator.validateLoanPeriodicAccrualData(command.json());
        LocalDate tillDate = command.localDateValueOfParameterNamed(accrueTillParamName);
        try {
            this.loanAccrualPlatformService.addPeriodicAccruals(tillDate);
        } catch (MultiException e) {
            final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
            final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors)
                    .resource(PERIODIC_ACCRUAL_ACCOUNTING_RESOURCE_NAME);
            baseDataValidator.reset().failWithCodeNoParameterAddedToErrorCode(PERIODIC_ACCRUAL_ACCOUNTING_EXECUTION_ERROR_CODE,
                    e.getMessage());
            throw new PlatformApiDataValidationException(dataValidationErrors, e);
        }
        return CommandProcessingResult.empty();
    }
}
