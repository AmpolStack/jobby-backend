package com.jobby.domain.ports;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;

import java.time.Duration;

public interface CacheService {
    <T> Result<Void, Error> put(String key, T value, Duration ttl);
    <T> Result<T, Error> get(String key, Class<T> type);
    Result<Void, Error> evict(String key);
}
