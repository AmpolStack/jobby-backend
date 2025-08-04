package com.jobby.authorization.application.ports.out;

import com.jobby.authorization.domain.result.Error;
import com.jobby.authorization.domain.result.Result;

import java.time.Duration;

public interface CacheService {
    <T> Result<Void, Error> put(String key, T value, Duration ttl);
    <T> Result<T, Error> get(String key, Class<T> type);
    Result<Void, Error> evict(String key);
}
