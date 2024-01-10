package com.btb.odj.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    public static final String CACHE_DRIVERS = "drivers";
    public static final String CACHE_RACES = "races";

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(CACHE_DRIVERS, CACHE_RACES);
        cacheManager.setCaffeine(
                Caffeine.newBuilder()
                        .maximumSize(0)
                        .expireAfterWrite(60, TimeUnit.MINUTES)
        );
        return cacheManager;
    }

}