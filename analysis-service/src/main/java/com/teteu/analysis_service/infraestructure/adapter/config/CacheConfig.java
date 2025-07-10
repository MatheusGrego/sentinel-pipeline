package com.teteu.analysis_service.infraestructure.adapter.config;

import com.teteu.analysis_service.infraestructure.adapter.in.web.constants.ApiConstants;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
        cacheManager.setCacheNames(java.util.List.of(ApiConstants.CACHE_ANALYSIS_STATS));
        return cacheManager;
    }
}