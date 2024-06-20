package com.fineract.mifos.mifos_core.infrastructure.cache.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.annotation.CachingConfigurer;



@EnableCaching
@Configuration
public class PlatformCacheConfiguration implements CachingConfigurer {

    @Autowired
    private RuntimeDelegatingCacheManager delegatingCacheManager;

    @Bean
    @Override
    public CacheManager cacheManager() {
        return this.delegatingCacheManager;
    }
}
