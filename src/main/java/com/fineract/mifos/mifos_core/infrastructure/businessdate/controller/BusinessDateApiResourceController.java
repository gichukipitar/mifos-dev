package com.fineract.mifos.mifos_core.infrastructure.businessdate.controller;

import com.fineract.mifos.mifos_core.commands.dtos.CommandWrapper;
import com.fineract.mifos.mifos_core.commands.service.CommandWrapperBuilder;
import com.fineract.mifos.mifos_core.commands.service.PortfolioCommandSourceWritePlatformService;
import com.fineract.mifos.mifos_core.infrastructure.businessdate.dto.BusinessDateData;
import com.fineract.mifos.mifos_core.infrastructure.core.data.CommandProcessingResult;
import com.fineract.mifos.mifos_core.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import com.fineract.mifos.mifos_core.infrastructure.security.service.PlatformSecurityContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@RestController
@RequestMapping("/v1/businessdate")
@RequiredArgsConstructor
@Tag(name = "Business Date Management", description = "Business date management enables you to set up, fetch and adjust organisation business dates")

public class BusinessDateApiResourceController {
    private final ApiRequestParameterHelper parameterHelper;
    private final PlatformSecurityContext securityContext;
    private final DefaultToApiJsonSerializer<BusinessDateData> jsonSerializer;
    private final BusinessDateReadPlatformService readPlatformService;
    private final PortfolioCommandSourceWritePlatformService commandWritePlatformService;

    @GetMapping(produces = "application/json")
    @Operation(summary = "List all business dates", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = BusinessDateApiResourceSwagger.BusinessDateResponse.class))))})
    public String getBusinessDates(@Context final UriInfo uriInfo) {
        securityContext.authenticatedUser().validateHasReadPermission("BUSINESS_DATE");
        final List<BusinessDateData> foundBusinessDates = this.readPlatformService.findAll();
        ApiRequestJsonSerializationSettings settings = parameterHelper.process(uriInfo.getQueryParameters());
        return this.jsonSerializer.serialize(settings, foundBusinessDates);
    }

    @GetMapping(path = "{type}", produces = "application/json")
    @Operation(summary = "Retrieve a specific Business date", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = BusinessDateApiResourceSwagger.BusinessDateResponse.class)))})
    public String getBusinessDate(@PathParam("type") @Parameter(description = "type") final String type, @Context final UriInfo uriInfo) {
        securityContext.authenticatedUser().validateHasReadPermission("BUSINESS_DATE");
        final BusinessDateData businessDate = this.readPlatformService.findByType(type);
        ApiRequestJsonSerializationSettings settings = parameterHelper.process(uriInfo.getQueryParameters());
        return this.jsonSerializer.serialize(settings, businessDate);
    }

    @PostMapping(path = "/update", consumes = "application/json")
    @Operation(summary = "Update Business Date", description = "")
    @RequestBody(required = true, content = @Content(schema = @Schema(implementation = BusinessDateApiResourceSwagger.BusinessDateRequest.class)))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = BusinessDateApiResourceSwagger.BusinessDateResponse.class))) })
    public ResponseEntity<String> updateBusinessDate(
            @RequestBody String jsonRequestBody,
            UriComponentsBuilder uriComponentsBuilder) {

        securityContext.authenticatedUser().validateHasUpdatePermission("BUSINESS_DATE");

        CommandWrapper commandRequest = new CommandWrapperBuilder()
                .updateBusinessDate()
                .withJson(jsonRequestBody)
                .build();

        CommandProcessingResult result = commandWritePlatformService.logCommandSource(commandRequest);
        String responseJson = jsonSerializer.serialize(result);

        return ResponseEntity.ok(responseJson);
    }
}
