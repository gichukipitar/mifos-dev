package com.fineract.mifos.mifos_core.infrastructure.configuration.service;

import com.fineract.mifos.mifos_core.infrastructure.cache.dto.CacheType;

import java.time.LocalDate;

public interface ConfigurationDomainService {

    boolean isMakerCheckerEnabledForTask(String taskPermissionCode);

    boolean isSameMakerCheckerEnabled();

    boolean isAmazonS3Enabled();

    boolean isRescheduleFutureRepaymentsEnabled();

    boolean isRescheduleRepaymentsOnHolidaysEnabled();

    boolean allowTransactionsOnHolidayEnabled();

    boolean allowTransactionsOnNonWorkingDayEnabled();

    boolean isConstraintApproachEnabledForDatatables();

    boolean isEhcacheEnabled();

    void updateCache(CacheType cacheType);

    Long retrievePenaltyWaitPeriod();

    boolean isPasswordForcedResetEnable();

    Long retrievePasswordLiveTime();

    Long retrieveGraceOnPenaltyPostingPeriod();

    Long retrieveOpeningBalancesContraAccount();

    boolean isSavingsInterestPostingAtCurrentPeriodEnd();

    Integer retrieveFinancialYearBeginningMonth();

    Integer retrieveMinAllowedClientsInGroup();

    Integer retrieveMaxAllowedClientsInGroup();

    boolean isMeetingMandatoryForJLGLoans();

    int getRoundingMode();

    boolean isBackdatePenaltiesEnabled();

    boolean isOrganisationstartDateEnabled();

    LocalDate retrieveOrganisationStartDate();

    boolean isPaymentTypeApplicableForDisbursementCharge();

    boolean isInterestChargedFromDateSameAsDisbursementDate();

    boolean isSkippingMeetingOnFirstDayOfMonthEnabled();

    boolean isInterestToBeRecoveredFirstWhenGreaterThanEMI();

    boolean isPrincipalCompoundingDisabledForOverdueLoans();

    Long retreivePeroidInNumberOfDaysForSkipMeetingDate();

    boolean isChangeEmiIfRepaymentDateSameAsDisbursementDateEnabled();

    boolean isDailyTPTLimitEnabled();

    Long getDailyTPTLimit();

    void removeGlobalConfigurationPropertyDataFromCache(String propertyName);

    boolean isSMSOTPDeliveryEnabled();

    boolean isEmailOTPDeliveryEnabled();

    Integer retrieveOTPCharacterLength();

    Integer retrieveOTPLiveTime();

    boolean isSubRatesEnabled();

    boolean isFirstRepaymentDateAfterRescheduleAllowedOnHoliday();

    String getAccountMappingForPaymentType();

    String getAccountMappingForCharge();

    boolean isNextDayFixedDepositInterestTransferEnabledForPeriodEnd();

    boolean retrievePivotDateConfig();

    boolean isRelaxingDaysConfigForPivotDateEnabled();

    Long retrieveRelaxingDaysConfigForPivotDate();

    boolean isBusinessDateEnabled();

    boolean isCOBDateAdjustmentEnabled();

    boolean isReversalTransactionAllowed();

    Long retrieveExternalEventsPurgeDaysCriteria();

    Long retrieveProcessedCommandsPurgeDaysCriteria();

    Long retrieveRepaymentDueDays();

    Long retrieveRepaymentOverdueDays();

    boolean isExternalIdAutoGenerationEnabled();

    boolean isAddressEnabled();

    boolean isCOBBulkEventEnabled();

    Long retrieveExternalEventBatchSize();

    String retrieveReportExportS3FolderName();

    String getAccrualDateConfigForCharge();

    String getNextPaymentDateConfigForLoan();

}
