package com.fineract.mifos.mifos_core.infrastructure.core.config;

import org.mapstruct.*;

@MapperConfig(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR, builder = @Builder(disableBuilder = true), uses = {
        ExternalIdMapper.class }, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public class MapstructMapperConfig {
}
