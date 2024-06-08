package com.fineract.mifos.mifos_accounting.accrual.service;

import com.fineract.mifos.mifos_accounting.accrual.serialization.AccrualAccountingDataValidator;
import com.fineract.mifos.mifos_core.infrastructure.core.api.JsonCommand;
import com.fineract.mifos.mifos_core.infrastructure.core.data.CommandProcessingResult;
import com.fineract.mifos.mifos_core.infrastructure.core.dto.ApiParameterError;
import com.fineract.mifos.mifos_core.infrastructure.core.dto.DataValidatorBuilder;
import com.fineract.mifos.mifos_core.infrastructure.core.exception.MultiException;
import com.fineract.mifos.mifos_core.infrastructure.core.exception.PlatformApiDataValidationException;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.fineract.mifos.mifos_accounting.accrual.constants.AccrualAccountingConstants.*;

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
