package com.fineract.mifos.mifos_core.infrastructure.cache.service;

import com.fineract.mifos.mifos_core.commands.annotation.CommandType;
import com.fineract.mifos.mifos_core.commands.handler.NewCommandSourceHandler;
import com.fineract.mifos.mifos_core.infrastructure.cache.dto.CacheApiConstants;
import com.fineract.mifos.mifos_core.infrastructure.core.api.JsonCommand;
import com.fineract.mifos.mifos_core.infrastructure.core.data.CommandProcessingResult;
import com.fineract.mifos.mifos_core.infrastructure.core.dto.ApiParameterError;
import com.fineract.mifos.mifos_core.infrastructure.core.dto.DataValidatorBuilder;
import com.fineract.mifos.mifos_core.infrastructure.core.exception.InvalidJsonException;
import com.fineract.mifos.mifos_core.infrastructure.core.exception.PlatformApiDataValidationException;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.*;

@Service
@CommandType(entity = "CACHE", action = "UPDATE")
public class UpdateCacheCommandHandler implements NewCommandSourceHandler {

    private final CacheWritePlatformService cacheService;
    private static final Set<String> REQUEST_DATA_PARAMETERS = new HashSet<>(Arrays.asList(CacheApiConstants.CACHE_TYPE_PARAMETER));

    @Autowired
    public UpdateCacheCommandHandler(final CacheWritePlatformService cacheService) {
        this.cacheService = cacheService;
    }

    @Transactional
    @Override
    public CommandProcessingResult processCommand(final JsonCommand command) {

        final String json = command.json();

        if (StringUtils.isBlank(json)) {
            throw new InvalidJsonException();
        }

        final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
        command.checkForUnsupportedParameters(typeOfMap, json, REQUEST_DATA_PARAMETERS);

        final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors)
                .resource(CacheApiConstants.RESOURCE_NAME.toLowerCase());

        final int cacheTypeEnum = command.integerValueSansLocaleOfParameterNamed(CacheApiConstants.CACHE_TYPE_PARAMETER);
        baseDataValidator.reset().parameter(CacheApiConstants.CACHE_TYPE_PARAMETER).value(Integer.valueOf(cacheTypeEnum)).notNull()
                .isOneOfTheseValues(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3));

        if (!dataValidationErrors.isEmpty()) {
            throw new PlatformApiDataValidationException(dataValidationErrors);
        }

        final CacheType cacheType = CacheType.fromInt(cacheTypeEnum);

        final Map<String, Object> changes = this.cacheService.switchToCache(cacheType);

        return new CommandProcessingResultBuilder().withCommandId(command.commandId()).with(changes).build();
    }
}
