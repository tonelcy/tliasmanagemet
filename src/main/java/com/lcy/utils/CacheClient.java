package com.lcy.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Slf4j
@Component
public class CacheClient {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DistributedLock distributedLock;

    @Autowired
    private CacheMetrics cacheMetrics;

    private static final long LOCK_WAIT_TIME = 10;
    private static final long LOCK_LEASE_TIME = 30;

    public void set(String key, Object value, long time, TimeUnit unit) {
        try {
            String jsonValue = objectMapper.writeValueAsString(value);
            stringRedisTemplate.opsForValue().set(key, jsonValue, time, unit);
        } catch (JsonProcessingException e) {
            log.error("序列化失败: key={}", key, e);
        }
    }

    public <T> T get(String key, Class<T> type) {
        String jsonValue = stringRedisTemplate.opsForValue().get(key);
        if (jsonValue == null) {
            return null;
        }
        try {
            return objectMapper.readValue(jsonValue, type);
        } catch (JsonProcessingException e) {
            log.error("反序列化失败: key={}", key, e);
            return null;
        }
    }

    public <T> T queryWithCacheAside(String key, Class<T> type, Function<Void, T> dbFallback, long time, TimeUnit unit) {
        T result = get(key, type);
        if (result != null) {
            cacheMetrics.recordCacheHit();
            return result;
        }

        cacheMetrics.recordCacheMiss();
        result = dbFallback.apply(null);

        if (result != null) {
            set(key, result, time, unit);
        }

        return result;
    }

    public <T> T queryWithMutex(String key, Class<T> type, Function<Void, T> dbFallback, long time, TimeUnit unit) {
        T result = get(key, type);
        if (result != null) {
            cacheMetrics.recordCacheHit();
            return result;
        }

        cacheMetrics.recordCacheMiss();

        String lockKey = "lock:" + key;
        
        return distributedLock.lockAndExecute(lockKey, LOCK_WAIT_TIME, LOCK_LEASE_TIME, () -> {
            T lockedResult = get(key, type);
            if (lockedResult != null) {
                return lockedResult;
            }

            T dbResult = dbFallback.apply(null);

            if (dbResult != null) {
                long randomTime = time + (long) (Math.random() * time / 2);
                set(key, dbResult, randomTime, unit);
            } else {
                set(key, "", 2, TimeUnit.MINUTES);
            }

            return dbResult;
        });
    }

    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }

    public void deleteByPattern(String pattern) {
        stringRedisTemplate.delete(stringRedisTemplate.keys(pattern));
    }

    public void updateWithCacheAside(String key, Runnable dbUpdate) {
        dbUpdate.run();
        delete(key);
    }

    public Boolean hasKey(String key) {
        return stringRedisTemplate.hasKey(key);
    }

    public Long increment(String key) {
        return stringRedisTemplate.opsForValue().increment(key);
    }

    public Boolean expire(String key, long time, TimeUnit unit) {
        return stringRedisTemplate.expire(key, time, unit);
    }
}
