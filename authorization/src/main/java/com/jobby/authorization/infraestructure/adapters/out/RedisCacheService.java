package com.jobby.authorization.infraestructure.adapters.out;

import com.jobby.authorization.domain.ports.out.CacheService;
import com.jobby.authorization.domain.shared.errors.Error;
import com.jobby.authorization.domain.shared.errors.ErrorType;
import com.jobby.authorization.domain.shared.errors.Field;
import com.jobby.authorization.domain.shared.result.Result;
import com.jobby.authorization.domain.shared.validators.StringValidator;
import com.jobby.authorization.domain.shared.validators.ValidationChain;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Service;
import java.time.Duration;

@Service
public class RedisCacheService implements CacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisCacheService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private static final Result<?, Error> REDIS_CONNECTION_FAILURE_RESULT =  Result.failure(
            ErrorType.ITS_EXTERNAL_SERVICE_FAILURE,
            new Field(
                    "redis",
                    "Error connection with redis"
            )
    );

    @Override
    public <T> Result<Void, Error> put(String key, T value, Duration ttl) {
        return ValidationChain.create()
                .validateInternalNotBlank(key, "cache-key")
                .build()
                .flatMap(x -> {
                    try{
                        redisTemplate.opsForValue().set(key, value, ttl);
                    }
                    catch(SerializationException e){
                        return Result.failure(
                                ErrorType.ITS_SERIALIZATION_ERROR,
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
        return ValidationChain.create()
                .validateInternalNotBlank(key, "cache-key")
                .build()
                .flatMap(x -> {
                    Object value;
                    try{
                        value = redisTemplate.opsForValue().get(key);
                    }
                    catch(SerializationException e){
                        return Result.failure(ErrorType.ITS_SERIALIZATION_ERROR,
                                new Field(
                                        "deserialization",
                                        "deserialization failed, the object provided are invalid to deserialize in the specified class"
                                )
                        );
                    }
                    catch(RedisConnectionFailureException e){
                        return Result.renewFailure(REDIS_CONNECTION_FAILURE_RESULT);
                    }

                    if(value == null){
                        return Result.failure(ErrorType.USER_NOT_FOUND, new Field("value", "the object consulted are null"));
                    }

                    T response;
                    try{
                        response = type.cast(value);
                    }
                    catch(ClassCastException e){
                        return Result.failure(
                                ErrorType.ITS_OPERATION_ERROR,
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
        return ValidationChain.create()
                .validateInternalNotBlank(key, "cache-key")
                .build()
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
