package com.fineract.mifos.mifos_core.infrastructure.event.external.serialization;

import com.fineract.mifos.mifos_core.infrastructure.core.exception.InvalidJsonException;
import com.fineract.mifos.mifos_core.infrastructure.core.serialization.AbstractFromApiJsonDeserializer;
import com.fineract.mifos.mifos_core.infrastructure.core.serialization.FromJsonHelper;
import com.fineract.mifos.mifos_core.infrastructure.event.external.command.ExternalEventConfigurationCommand;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
@AllArgsConstructor
public class ExternalEventConfigurationCommandFromApiJsonDeserializer
        extends AbstractFromApiJsonDeserializer<ExternalEventConfigurationCommand> {

    private static final String EXTERNAL_EVENT_CONFIGURATIONS = "externalEventConfigurations";
    private final Set<String> supportedParameters = new HashSet<>(Arrays.asList(EXTERNAL_EVENT_CONFIGURATIONS));
    private final FromJsonHelper fromApiJsonHelper;

    @Override
    public ExternalEventConfigurationCommand commandFromApiJson(String json) {
        if (StringUtils.isBlank(json)) {
            throw new InvalidJsonException();
        }

        final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
        fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json, supportedParameters);

        return fromApiJsonHelper.fromJson(json, ExternalEventConfigurationCommand.class);
    }
}
