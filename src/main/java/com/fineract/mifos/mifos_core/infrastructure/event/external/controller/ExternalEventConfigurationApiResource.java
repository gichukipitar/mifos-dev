package com.fineract.mifos.mifos_core.infrastructure.event.external.controller;

import com.fineract.mifos.mifos_core.commands.dtos.CommandWrapper;
import com.fineract.mifos.mifos_core.commands.service.CommandWrapperBuilder;
import com.fineract.mifos.mifos_core.commands.service.PortfolioCommandSourceWritePlatformService;
import com.fineract.mifos.mifos_core.infrastructure.core.api.ApiRequestParameterHelper;
import com.fineract.mifos.mifos_core.infrastructure.core.data.CommandProcessingResult;
import com.fineract.mifos.mifos_core.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import com.fineract.mifos.mifos_core.infrastructure.security.service.PlatformSecurityContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/externalevents/configuration")
@Tag(name = "External event configuration", description = "External event configuration enables user to enable/disable event posting to downstream message channel")

public class ExternalEventConfigurationApiResource {
    private static final Set<String> RESPONSE_DATA_PARAMETERS = new HashSet<>(Arrays.asList("type", "enabled"));

    private static final String RESOURCE_NAME_FOR_PERMISSIONS = "EXTERNAL_EVENT_CONFIGURATION";

    private final PlatformSecurityContext context;
    private final ApiRequestParameterHelper apiRequestParameterHelper;
    private final PortfolioCommandSourceWritePlatformService commandWritePlatformService;
    private final DefaultToApiJsonSerializer<ExternalEventConfigurationData> jsonSerializer;
    private final ExternalEventConfigurationReadPlatformService readPlatformService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "List all external event configurations", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of all external event configurations", content = @Content(schema = @Schema(implementation = ExternalEventConfigurationApiResourceSwagger.GetExternalEventConfigurationsResponse.class)))})
    public String retrieveExternalEventConfiguration(@Context final UriInfo uriInfo) {
        context.authenticatedUser().validateHasReadPermission(RESOURCE_NAME_FOR_PERMISSIONS);
        final ExternalEventConfigurationData configurationData = readPlatformService.findAllExternalEventConfigurations();
        final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
        return jsonSerializer.serialize(settings, configurationData, RESPONSE_DATA_PARAMETERS);
    }
@PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@Operation(summary = "Enable/Disable external events posting", description = "")
@RequestBody(required = true, content = @Content(schema = @Schema(implementation = ExternalEventConfigurationApiResourceSwagger.PutExternalEventConfigurationsRequest.class)))
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = CommandProcessingResult.class))) })

public String updateExternalEventConfigurationsDetails(@Parameter(hidden = true) final String apiRequestBodyAsJson) {
        context.authenticatedUser().validateHasUpdatePermission(RESOURCE_NAME_FOR_PERMISSIONS);
        final CommandWrapper commandRequest = new CommandWrapperBuilder() //
                .updateExternalEventConfigurations() //
                .withJson(apiRequestBodyAsJson) //
                .build();
        final CommandProcessingResult result = this.commandWritePlatformService.logCommandSource(commandRequest);
        return this.jsonSerializer.serialize(result);

    }

}
