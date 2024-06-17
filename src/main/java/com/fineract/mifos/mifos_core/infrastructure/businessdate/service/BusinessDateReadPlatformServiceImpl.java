package com.fineract.mifos.mifos_core.infrastructure.businessdate.service;

import com.fineract.mifos.mifos_core.infrastructure.businessdate.dto.BusinessDateData;
import com.fineract.mifos.mifos_core.infrastructure.businessdate.dto.BusinessDateType;
import com.fineract.mifos.mifos_core.infrastructure.businessdate.entity.BusinessDate;
import com.fineract.mifos.mifos_core.infrastructure.businessdate.exception.BusinessDateNotFoundException;
import com.fineract.mifos.mifos_core.infrastructure.businessdate.mapper.BusinessDateMapper;
import com.fineract.mifos.mifos_core.infrastructure.businessdate.repository.BusinessDateRepository;
import com.fineract.mifos.mifos_core.infrastructure.core.service.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BusinessDateReadPlatformServiceImpl implements BusinessDateReadPlatformService {
    private final BusinessDateRepository repository;
    private final BusinessDateMapper mapper;
    private final ConfigurationDomainService configurationDomainService;

    @Override
    public List<BusinessDateData> findAll() {
        List<BusinessDate> businessDateList = repository.findAll();
        return mapper.map(businessDateList);
    }

    @Override
    public BusinessDateData findByType(String type) {
        BusinessDateType businessDateType;
        try {
            businessDateType = BusinessDateType.valueOf(type);
        } catch (IllegalArgumentException e) {
            log.error("Provided business date type cannot be found: {}", type);
            throw BusinessDateNotFoundException.notExist(type, e);
        }
        Optional<BusinessDate> businessDate = repository.findByType(businessDateType);
        if (businessDate.isEmpty()) {
            log.error("Business date with the provided type cannot be found {}", type);
            throw BusinessDateNotFoundException.notFound(type);
        }
        return mapper.map(businessDate.get());
    }

    @Override
    public HashMap<BusinessDateType, LocalDate> getBusinessDates() {
        HashMap<BusinessDateType, LocalDate> businessDateMap = new HashMap<>();
        LocalDate tenantDate = DateUtils.getLocalDateOfTenant();
        businessDateMap.put(BusinessDateType.BUSINESS_DATE, tenantDate);
        businessDateMap.put(BusinessDateType.COB_DATE, tenantDate);
        if (configurationDomainService.isBusinessDateEnabled()) {
            final List<BusinessDateData> businessDateDataList = this.findAll();
            for (BusinessDateData businessDateData : businessDateDataList) {
                businessDateMap.put(BusinessDateType.valueOf(businessDateData.getType()), businessDateData.getDate());
            }
        }
        return businessDateMap;
    }

}
