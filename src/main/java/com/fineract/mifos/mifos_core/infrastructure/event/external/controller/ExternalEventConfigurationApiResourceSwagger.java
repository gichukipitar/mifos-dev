package com.fineract.mifos.mifos_core.infrastructure.event.external.controller;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;

public class ExternalEventConfigurationApiResourceSwagger {

    private ExternalEventConfigurationApiResourceSwagger() {}

    @Schema(description = "GetExternalEventConfigurationsResponse")
    public static final class GetExternalEventConfigurationsResponse {

        private GetExternalEventConfigurationsResponse() {}

        public List<ExternalEventConfigurationItemData> externalEventConfiguration;
    }

    @Schema(description = "PutExternalEventConfigurationsRequest")
    public static final class PutExternalEventConfigurationsRequest {

        private PutExternalEventConfigurationsRequest() {}

        @Schema(example = "\"CentersCreateBusinessEvent\":true,\n" + "\"ClientActivateBusinessEvent\":true")
        public Map<String, Boolean> externalEventConfigurations;

    }

}
