package com.gngsn.map.common.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.gngsn.map.common.cd.CacheType.KEYWORD_PLACE_SEARCH;
import static com.gngsn.map.common.cd.CacheType.SEARCH_KEYWORD_RANK;

/**
 * Cache Configuration (Caffeine Cache)
 */
@Configuration
@EnableCaching
public class CacheConfiguration {

    public Cache<Object, Object> createCaffeine(final long expireSeconds) {
        return Caffeine.newBuilder().recordStats().expireAfterWrite(expireSeconds, TimeUnit.SECONDS).build();
    }

    @Bean
    public CacheManager cacheManager() {
        final SimpleCacheManager cacheManager = new SimpleCacheManager();

        final List<CaffeineCache> caches = List.of(
                new CaffeineCache(SEARCH_KEYWORD_RANK.getCacheName(), createCaffeine(SEARCH_KEYWORD_RANK.getExpireSeconds())),
                new CaffeineCache(KEYWORD_PLACE_SEARCH.getCacheName(), createCaffeine(KEYWORD_PLACE_SEARCH.getExpireSeconds()))
        );

        cacheManager.setCaches(caches);
        return cacheManager;
    }
}
