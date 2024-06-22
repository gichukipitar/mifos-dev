package com.fineract.mifos.mifos_core.infrastructure.cache.controller;

import com.fineract.mifos.mifos_core.commands.dtos.CommandWrapper;
import com.fineract.mifos.mifos_core.commands.service.CommandWrapperBuilder;
import com.fineract.mifos.mifos_core.commands.service.PortfolioCommandSourceWritePlatformService;
import com.fineract.mifos.mifos_core.infrastructure.cache.dto.CacheData;
import com.fineract.mifos.mifos_core.infrastructure.cache.service.RuntimeDelegatingCacheManager;
import com.fineract.mifos.mifos_core.infrastructure.core.data.CommandProcessingResult;
import com.fineract.mifos.mifos_core.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import com.fineract.mifos.mifos_core.infrastructure.core.serialization.DefaultToApiJsonSerializer;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/v1/caches")
@Tag(name = "Cache", description = "The following settings are possible for cache:\n" + "\n" + "No Caching: caching turned off\n"
        + "Single node: caching on for single instance deployments of platform (works for multiple tenants but only one tomcat)\n"
        + "By default caching is set to No Caching. Switching between caches results in the cache been clear e.g. from Single node to No cache and back again would clear down the single node cache.")
@RequiredArgsConstructor
public class CacheApiResourceController {
    private static final Set<String> RESPONSE_DATA_PARAMETERS = new HashSet<>(List.of("id"));
    private static final String RESOURCE_NAME_FOR_PERMISSIONS = "CACHE";

    private final PlatformSecurityContext context;
    private final DefaultToApiJsonSerializer<CacheData> toApiJsonSerializer;
    private final ApiRequestParameterHelper apiRequestParameterHelper;
    private final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;

    @Qualifier("runtimeDelegatingCacheManager")

    private final RuntimeDelegatingCacheManager cacheService;

    @GetMapping
    @Operation(summary = "Retrieve Cache Types", description = "Returns the list of caches.\n" + "\n" + "Example Requests:\n" + "\n"
            + "caches")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CacheApiResourceSwagger.GetCachesResponse.class)))) })
    public String retrieveAll(@Context final UriInfo uriInfo) {

        this.context.authenticatedUser().validateHasReadPermission(RESOURCE_NAME_FOR_PERMISSIONS);

        final Collection<CacheData> codes = this.cacheService.retrieveAll();

        final ApiRequestJsonSerializationSettings settings = this.apiRequestParameterHelper.process(uriInfo.getQueryParameters());
        return this.toApiJsonSerializer.serialize(settings, codes, RESPONSE_DATA_PARAMETERS);
    }

@PutMapping
@Operation(summary = "Switch Cache", description = "Switches the cache to chosen one.")
@RequestBody(required = true, content = @Content(schema = @Schema(implementation = CacheApiResourceSwagger.PutCachesRequest.class)))
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = CacheApiResourceSwagger.PutCachesResponse.class))) })
public String switchCache(@Parameter(hidden = true) final String apiRequestBodyAsJson) {

    final CommandWrapper commandRequest = new CommandWrapperBuilder().updateCache().withJson(apiRequestBodyAsJson).build();

    final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);

    return this.toApiJsonSerializer.serialize(result);
}

}
