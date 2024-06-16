package com.fineract.mifos.mifos_core.infrastructure.businessdate.controller;

import com.fineract.mifos.mifos_core.commands.service.PortfolioCommandSourceWritePlatformService;
import com.fineract.mifos.mifos_core.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import com.fineract.mifos.mifos_core.infrastructure.security.service.PlatformSecurityContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping( produces = MediaType.APPLICATION_JSON)
    @Operation(summary = "List all business dates", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = BusinessDateApiResourceSwagger.BusinessDateResponse.class)))) })
    public String getBusinessDates(@Context final UriInfo uriInfo) {
        securityContext.authenticatedUser().validateHasReadPermission("BUSINESS_DATE");
        final List<BusinessDateData> foundBusinessDates = this.readPlatformService.findAll();
        ApiRequestJsonSerializationSettings settings = parameterHelper.process(uriInfo.getQueryParameters());
        return this.jsonSerializer.serialize(settings, foundBusinessDates);
    }
}
