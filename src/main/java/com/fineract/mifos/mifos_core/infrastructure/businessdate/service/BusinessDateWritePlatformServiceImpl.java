package com.fineract.mifos.mifos_core.infrastructure.businessdate.service;

import com.fineract.mifos.mifos_core.infrastructure.businessdate.dto.BusinessDateData;
import com.fineract.mifos.mifos_core.infrastructure.businessdate.dto.BusinessDateType;
import com.fineract.mifos.mifos_core.infrastructure.businessdate.entity.BusinessDate;
import com.fineract.mifos.mifos_core.infrastructure.businessdate.exception.BusinessDateActionException;
import com.fineract.mifos.mifos_core.infrastructure.businessdate.repository.BusinessDateRepository;
import com.fineract.mifos.mifos_core.infrastructure.businessdate.validator.BusinessDateDataParserAndValidator;
import com.fineract.mifos.mifos_core.infrastructure.core.api.JsonCommand;
import com.fineract.mifos.mifos_core.infrastructure.core.data.CommandProcessingResult;
import com.fineract.mifos.mifos_core.infrastructure.core.data.CommandProcessingResultBuilder;
import com.fineract.mifos.mifos_core.infrastructure.core.dto.ApiParameterError;
import com.fineract.mifos.mifos_core.infrastructure.core.exception.AbstractPlatformDomainRuleException;
import com.fineract.mifos.mifos_core.infrastructure.core.exception.PlatformApiDataValidationException;
import com.fineract.mifos.mifos_core.infrastructure.core.service.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BusinessDateWritePlatformServiceImpl implements BusinessDateWritePlatformService{

    private final BusinessDateDataParserAndValidator dataValidator;
    private final BusinessDateRepository repository;
    private final ConfigurationDomainService configurationDomainService;

    @Override
    public CommandProcessingResult updateBusinessDate(@NotNull final JsonCommand command) {
        BusinessDateData data = dataValidator.validateAndParseUpdate(command);
        Map<String, Object> changes = new HashMap<>();
        adjustDate(data, changes);
        return new CommandProcessingResultBuilder().withCommandId(command.commandId()).with(changes).build();

    }

    @Override
    public void adjustDate(BusinessDateData data, Map<String, Object> changes) {
        boolean isCOBDateAdjustmentEnabled = configurationDomainService.isCOBDateAdjustmentEnabled();
        boolean isBusinessDateEnabled = configurationDomainService.isBusinessDateEnabled();

        if (!isBusinessDateEnabled) {
            log.error("Business date functionality is not enabled!");
            throw new BusinessDateActionException("business.date.is.not.enabled", "Business date functionality is not enabled");
        }
        updateOrCreateBusinessDate(data.getType(), data.getDate(), changes);
        if (isCOBDateAdjustmentEnabled && BusinessDateType.BUSINESS_DATE.name().equals(data.getType())) {
            updateOrCreateBusinessDate(BusinessDateType.COB_DATE.getName(), data.getDate().minus(1, ChronoUnit.DAYS), changes);
        }
    }

    @Override
    public void increaseCOBDateByOneDay() throws JobExecutionException {
        increaseDateByTypeByOneDay(BusinessDateType.COB_DATE);
    }

    @Override
    public void increaseBusinessDateByOneDay() throws JobExecutionException {
        increaseDateByTypeByOneDay(BusinessDateType.BUSINESS_DATE);
    }

    private void increaseDateByTypeByOneDay(BusinessDateType businessDateType) throws JobExecutionException {
        Map<String, Object> changes = new HashMap<>();
        Optional<BusinessDate> businessDateEntity = repository.findByType(businessDateType);
        List<Throwable> exceptions = new ArrayList<>();

        LocalDate businessDate = businessDateEntity.map(BusinessDate::getDate).orElse(DateUtils.getLocalDateOfTenant());
        businessDate = businessDate.plusDays(1);
        try {
            BusinessDateData businessDateData = BusinessDateData.instance(businessDateType, businessDate);
            adjustDate(businessDateData, changes);
        } catch (final PlatformApiDataValidationException e) {
            final List<ApiParameterError> errors = e.getErrors();
            for (final ApiParameterError error : errors) {
                log.error("Increasing {} by 1 day failed due to: {}", businessDateType.getDescription(), error.getDeveloperMessage());
            }
            exceptions.add(e);
        } catch (final AbstractPlatformDomainRuleException e) {
            log.error("Increasing {} by 1 day failed due to: {}", businessDateType.getDescription(), e.getDefaultUserMessage());
            exceptions.add(e);
        } catch (Exception e) {
            log.error("Increasing {} by 1 day failed due to: {}", businessDateType.getDescription(), e.getMessage());
            exceptions.add(e);
        }
        if (!exceptions.isEmpty()) {
            throw new JobExecutionException(exceptions.toString());
        }
    }

    private void updateOrCreateBusinessDate(String type, LocalDate newDate, Map<String, Object> changes) {
        BusinessDateType businessDateType = BusinessDateType.valueOf(type);
        Optional<BusinessDate> businessDate = repository.findByType(businessDateType);

        if (businessDate.isEmpty()) {
            BusinessDate newBusinessDate = BusinessDate.instance(businessDateType, newDate);
            repository.save(newBusinessDate);
            changes.put(type, newBusinessDate.getDate());
        } else {
            updateBusinessDate(businessDate.get(), newDate, changes);
        }
    }

    private void updateBusinessDate(BusinessDate businessDate, LocalDate newDate, Map<String, Object> changes) {
        LocalDate oldDate = businessDate.getDate();

        if (DateUtils.isEqual(oldDate, newDate)) {
            return;
        }
        businessDate.setDate(newDate);
        repository.save(businessDate);
        changes.put(businessDate.getType().name(), newDate);
    }

}
