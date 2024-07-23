package com.fineract.mifos.mifos_core.infrastructure.event.external.service;

import com.fineract.mifos.mifos_core.infrastructure.core.config.MapstructMapperConfig;
import com.fineract.mifos.mifos_core.infrastructure.event.external.dto.ExternalEventConfigurationItemData;
import com.fineract.mifos.mifos_core.infrastructure.event.external.entity.ExternalEventConfiguration;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = MapstructMapperConfig.class)
public interface ExternalEventsConfigurationMapper {

    List<ExternalEventConfigurationItemData> map(List<ExternalEventConfiguration> source);
}
