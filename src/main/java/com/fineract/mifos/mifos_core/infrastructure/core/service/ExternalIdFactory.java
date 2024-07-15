package com.fineract.mifos.mifos_core.infrastructure.core.service;

import com.fineract.mifos.mifos_core.infrastructure.configuration.service.ConfigurationDomainService;
import com.fineract.mifos.mifos_core.infrastructure.core.api.JsonCommand;
import com.fineract.mifos.mifos_core.infrastructure.core.domain.ExternalId;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExternalIdFactory {

    private final ConfigurationDomainService configurationDomainService;

    public static ExternalId produce(String value) {
        return StringUtils.isBlank(value) ? ExternalId.empty() : new ExternalId(value);
    }

    public ExternalId createFromCommand(JsonCommand command, final String externalIdKey) {
        String externalIdStr = null;
        if (command.parsedJson() != null) {
            externalIdStr = command.stringValueOfParameterNamedAllowingNull(externalIdKey);
        }
        return create(externalIdStr);
    }

    public ExternalId create(String externalIdStr) {
        if (StringUtils.isBlank(externalIdStr)) {
            if (configurationDomainService.isExternalIdAutoGenerationEnabled()) {
                return ExternalId.generate();
            } else {
                return ExternalId.empty();
            }
        }
        return new ExternalId(externalIdStr);
    }

    public ExternalId create() {
        return create(null);
    }
}
