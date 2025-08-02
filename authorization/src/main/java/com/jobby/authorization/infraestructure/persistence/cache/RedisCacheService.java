package com.jobby.authorization.infraestructure.persistence.cache;

import com.jobby.authorization.application.ports.out.CacheService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.Optional;

@Service
public class RedisCacheService implements CacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisCacheService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public <T> void put(String key, T value, long ttlInSeconds) {
        redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(ttlInSeconds));
    }

    @Override
    public <T> Optional<T> get(String key, Class<T> type) {
        var value = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(type.cast(value));
    }

    @Override
    public void evict(String key) {
        redisTemplate.delete(key);
    }
}
