package com.fineract.mifos.mifos_core.infrastructure.cache.service;

import com.fineract.mifos.mifos_core.infrastructure.cache.dto.CacheType;

import java.util.Map;

public interface CacheWritePlatformService {
    Map<String, Object> switchToCache(CacheType cacheType);
}
