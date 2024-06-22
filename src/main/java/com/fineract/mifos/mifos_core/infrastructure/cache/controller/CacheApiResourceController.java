package com.fineract.mifos.mifos_core.infrastructure.cache.controller;

import com.fineract.mifos.mifos_core.commands.service.PortfolioCommandSourceWritePlatformService;
import com.fineract.mifos.mifos_core.infrastructure.cache.dto.CacheData;
import com.fineract.mifos.mifos_core.infrastructure.cache.service.RuntimeDelegatingCacheManager;
import com.fineract.mifos.mifos_core.infrastructure.core.serialization.DefaultToApiJsonSerializer;
import com.fineract.mifos.mifos_core.infrastructure.security.service.PlatformSecurityContext;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
