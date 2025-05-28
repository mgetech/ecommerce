package com.example.user.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class CacheLoggingAspect {

    @Before("@annotation(cacheable) && args(id,..)")
    public void logCacheAccess(JoinPoint joinPoint, Cacheable cacheable, Long id) {
        String cacheName = cacheable.cacheNames().length > 0 ? cacheable.cacheNames()[0] : "default";
        String key = id.toString();
        log.info("Attempting to access cache '{}' with key '{}'", cacheName, key);
    }

    @After("@annotation(cachePut) && args(id,..)")
    public void logCachePut(JoinPoint joinPoint, CachePut cachePut, Long id) {
        String cacheName = cachePut.cacheNames().length > 0 ? cachePut.cacheNames()[0] : "default";
        String key = id.toString();
        log.info("Updated cache '{}' with key '{}'", cacheName, key);
    }

    @After("@annotation(cacheEvict) && args(id,..)")
    public void logCacheEvict(JoinPoint joinPoint, CacheEvict cacheEvict, Long id) {
        String cacheName = cacheEvict.cacheNames().length > 0 ? cacheEvict.cacheNames()[0] : "default";
        String key = id.toString();
        log.info("Evicting cache '{}' with key '{}'", cacheName, key);
    }


}

