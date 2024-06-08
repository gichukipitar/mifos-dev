package com.fineract.mifos.mifos_accounting.accrual.serialization;

import com.fineract.mifos.mifos_accounting.accrual.constants.AccrualAccountingConstants;
import com.fineract.mifos.mifos_core.infrastructure.core.dto.ApiParameterError;
import com.fineract.mifos.mifos_core.infrastructure.core.dto.DataValidatorBuilder;
import com.fineract.mifos.mifos_core.infrastructure.core.exception.InvalidJsonException;
import com.fineract.mifos.mifos_core.infrastructure.core.exception.PlatformApiDataValidationException;
import com.fineract.mifos.mifos_core.infrastructure.core.serialization.FromJsonHelper;
import com.fineract.mifos.mifos_core.infrastructure.core.service.DateUtils;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.*;

import static com.fineract.mifos.mifos_accounting.accrual.constants.AccrualAccountingConstants.PERIODIC_ACCRUAL_ACCOUNTING_RESOURCE_NAME;
import static com.fineract.mifos.mifos_accounting.accrual.constants.AccrualAccountingConstants.accrueTillParamName;

@Component
@RequiredArgsConstructor
public final class AccrualAccountingDataValidator {

    private static final Set<String> LOAN_PERIODIC_REQUEST_DATA_PARAMETERS = new HashSet<>(
            Arrays.asList(accrueTillParamName, AccrualAccountingConstants.localeParamName, AccrualAccountingConstants.dateFormatParamName));

    private final FromJsonHelper fromApiJsonHelper;

    public void validateLoanPeriodicAccrualData(final String json) {
        if (StringUtils.isBlank(json)) {
            throw new InvalidJsonException();
        }
        final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
        this.fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json, LOAN_PERIODIC_REQUEST_DATA_PARAMETERS);

        final JsonElement element = this.fromApiJsonHelper.parse(json);
        final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors)
                .resource(PERIODIC_ACCRUAL_ACCOUNTING_RESOURCE_NAME);

        final LocalDate date = this.fromApiJsonHelper.extractLocalDateNamed(accrueTillParamName, element);
        baseDataValidator.reset().parameter(accrueTillParamName).value(date).notNull().validateDateBefore(DateUtils.getBusinessLocalDate());

        throwExceptionIfValidationWarningsExist(dataValidationErrors);

    }

    public void throwExceptionIfValidationWarningsExist(final List<ApiParameterError> dataValidationErrors) {
        if (!dataValidationErrors.isEmpty()) {
            throw new PlatformApiDataValidationException(dataValidationErrors);
        }
    }
}
