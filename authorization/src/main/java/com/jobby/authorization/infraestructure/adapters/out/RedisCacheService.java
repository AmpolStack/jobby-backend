package com.jobby.authorization.infraestructure.adapters.out;

import com.jobby.authorization.application.ports.out.CacheService;
import com.jobby.authorization.domain.result.Error;
import com.jobby.authorization.domain.result.ErrorType;
import com.jobby.authorization.domain.result.Field;
import com.jobby.authorization.domain.result.Result;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Service;
import java.time.Duration;

@Service
public class RedisCacheService implements CacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final Result<?, Error> REDIS_CONNECTION_FAILURE_RESULT =  Result.failure(
            ErrorType.ITN_EXTERNAL_SERVICE_FAILURE,
            new Field(
                    "redis",
                    "Error connection with redis"
            )
    );


    public RedisCacheService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private Result<Void, Error> validateKey(String key) {
        if(key == null || key.isBlank()){
            return Result.failure(
                    ErrorType.VALIDATION_ERROR,
                    new Field(
                            "key",
                            "the cache key is required"
                    )
            );
        }
        return Result.success(null);
    }

    @Override
    public <T> Result<Void, Error> put(String key, T value, Duration ttl) {
        return validateKey(key)
                .flatMap(x -> {
                    try{
                        redisTemplate.opsForValue().set(key, value, ttl);
                    }
                    catch(SerializationException e){
                        return Result.failure(
                                ErrorType.ITN_SERIALIZATION_ERROR,
                                new Field(
                                        "serialization",
                                        "serialization failed, the object provided are invalid to serialize"
                                )
                        );
                    }
                    catch(RedisConnectionFailureException e){
                        return Result.renewFailure(REDIS_CONNECTION_FAILURE_RESULT);
                    }
                    return Result.success(null);
                });
    }

    @Override
    public <T> Result<T, Error> get(String key, Class<T> type) {
        return validateKey(key)
                .flatMap(x -> {
                    Object value;
                    try{
                        value = redisTemplate.opsForValue().get(key);
                    }
                    catch(SerializationException e){
                        return Result.failure(ErrorType.ITN_SERIALIZATION_ERROR,
                                new Field(
                                        "deserialization",
                                        "deserialization failed, the object provided are invalid to deserialize in the specified class"
                                )
                        );
                    }
                    catch(RedisConnectionFailureException e){
                        return Result.renewFailure(REDIS_CONNECTION_FAILURE_RESULT);
                    }

                    T response;
                    try{
                        response = type.cast(value);
                    }
                    catch(ClassCastException e){
                        return Result.failure(
                                ErrorType.ITN_OPERATION_ERROR,
                                new Field(
                                        "type cast",
                                        "the object is not assignable to type"
                                )
                        );
                    }

                    return Result.success(response);
                });
    }

    @Override
    public Result<Void, Error> evict(String key) {
        return validateKey(key)
                .flatMap(x -> {
                    try{
                        redisTemplate.delete(key);
                    }
                    catch(RedisConnectionFailureException e){
                        return Result.renewFailure(REDIS_CONNECTION_FAILURE_RESULT);
                    }

                    return Result.success(null);
                });
    }
}
