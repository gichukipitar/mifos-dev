package com.fineract.mifos.mifos_core.infrastructure.cache.service;

import com.fineract.mifos.mifos_core.infrastructure.cache.dto.CacheApiConstants;
import com.fineract.mifos.mifos_core.infrastructure.cache.dto.CacheData;
import com.fineract.mifos.mifos_core.infrastructure.cache.dto.CacheType;
import com.fineract.mifos.mifos_core.infrastructure.core.dto.EnumOptionData;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * At present this implementation of {@link CacheManager} just delegates to the real {@link CacheManager} to use.
 *
 * By default it is {@link NoOpCacheManager} but we can change that by checking some persisted configuration in the
 * database on startup and allow user to switch implementation through UI/API
 */
@Component(value = "runtimeDelegatingCacheManager")
@RequiredArgsConstructor
@Slf4j

public class RuntimeDelegatingCacheManager implements CacheManager, InitializingBean {

    public Collection<CacheData> retrieveAll() {

        final boolean noCacheEnabled = currentCacheManager == defaultCacheManager;
        final boolean ehCacheEnabled = currentCacheManager == ehCacheManager;

        final EnumOptionData noCacheType = CacheEnumerations.cacheType(CacheType.NO_CACHE);
        final EnumOptionData singleNodeCacheType = CacheEnumerations.cacheType(CacheType.SINGLE_NODE);

        final CacheData noCache = CacheData.instance(noCacheType, noCacheEnabled);
        final CacheData singleNodeCache = CacheData.instance(singleNodeCacheType, ehCacheEnabled);

        return Arrays.asList(noCache, singleNodeCache);
    }

    public Map<String, Object> switchToCache(final boolean ehcacheEnabled, final CacheType toCacheType) {

        final Map<String, Object> changes = new HashMap<>();

        final boolean noCacheEnabled = !ehcacheEnabled;

        switch (toCacheType) {
            case INVALID -> {
                log.warn("Invalid cache type used");
            }
            case NO_CACHE -> {
                if (!noCacheEnabled) {
                    changes.put(CacheApiConstants.CACHE_TYPE_PARAMETER, toCacheType.getValue());
                }
                currentCacheManager = defaultCacheManager;
            }
            case SINGLE_NODE -> {
                if (!ehcacheEnabled) {
                    changes.put(CacheApiConstants.CACHE_TYPE_PARAMETER, toCacheType.getValue());
                    clearEhCache();
                }
                currentCacheManager = ehCacheManager;

                if (currentCacheManager.getCacheNames().isEmpty()) {
                    log.error("No caches configured for activated CacheManager {}", currentCacheManager);
                }
            }
            case MULTI_NODE -> throw new UnsupportedOperationException("Multi node cache is not supported");
        }

        return changes;
    }

    @SuppressFBWarnings(value = "DCN_NULLPOINTER_EXCEPTION", justification = "TODO: fix this!")
    private void clearEhCache() {
        Iterable<String> cacheNames = ehCacheManager.getCacheNames();
        for (String cacheName : cacheNames) {
            try {
                if (Objects.nonNull(ehCacheManager.getCache(cacheName))) {
                    Objects.requireNonNull(ehCacheManager.getCache(cacheName)).clear();
                }
            } catch (NullPointerException npe) {
                log.warn("NullPointerException occurred", npe);
            }
        }
    }

}
