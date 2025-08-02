package com.jobby.authorization.application.ports.out;

import java.util.Optional;

public interface CacheService {
    <T> void put(String key, T value, long ttlInSeconds);
    <T> Optional<T> get(String key, Class<T> type);
    void evict(String key);
}
