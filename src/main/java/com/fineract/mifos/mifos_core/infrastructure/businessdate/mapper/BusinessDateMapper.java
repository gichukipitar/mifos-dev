package com.fineract.mifos.mifos_core.infrastructure.businessdate.mapper;

import com.fineract.mifos.mifos_core.infrastructure.businessdate.dto.BusinessDateData;
import com.fineract.mifos.mifos_core.infrastructure.businessdate.entity.BusinessDate;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;


@Mapper(config = MapstructMapperConfig.class)
public interface BusinessDateMapper {
    @Mappings({ @Mapping(target = "description", source = "source.type.description") })
    BusinessDateData map(BusinessDate source);

    List<BusinessDateData> map(List<BusinessDate> sources);
}
