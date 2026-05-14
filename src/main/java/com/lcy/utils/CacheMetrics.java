package com.lcy.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Component
public class CacheMetrics {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private final AtomicLong cacheHitCount = new AtomicLong(0);
    private final AtomicLong cacheMissCount = new AtomicLong(0);

    public void recordCacheHit() {
        cacheHitCount.incrementAndGet();
    }

    public void recordCacheMiss() {
        cacheMissCount.incrementAndGet();
    }

    public double getHitRate() {
        long hits = cacheHitCount.get();
        long total = hits + cacheMissCount.get();
        return total == 0 ? 1.0 : (double) hits / total;
    }

    public Map<String, Object> getMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("cacheHitCount", cacheHitCount.get());
        metrics.put("cacheMissCount", cacheMissCount.get());
        metrics.put("cacheHitRate", String.format("%.2f%%", getHitRate() * 100));

        try {
            Properties info = stringRedisTemplate.execute((RedisCallback<Properties>) connection -> connection.info("server"));
            if (info != null) {
                metrics.put("redisVersion", info.getProperty("redis_version"));
                metrics.put("connectedClients", info.getProperty("connected_clients"));
            }

            Properties memoryInfo = stringRedisTemplate.execute((RedisCallback<Properties>) connection -> connection.info("memory"));
            if (memoryInfo != null) {
                metrics.put("usedMemory", memoryInfo.getProperty("used_memory_human"));
                metrics.put("usedMemoryRss", memoryInfo.getProperty("used_memory_rss_human"));
            }
        } catch (Exception e) {
            log.error("获取Redis指标失败", e);
        }

        return metrics;
    }

    public void resetMetrics() {
        cacheHitCount.set(0);
        cacheMissCount.set(0);
        log.info("缓存指标已重置");
    }

    public void logMetrics() {
        Map<String, Object> metrics = getMetrics();
        log.info("缓存指标: {}", metrics);
    }
}
