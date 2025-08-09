package com.jobby.authorization.infraestructure.adapters.out;

import com.jobby.authorization.domain.result.ErrorType;
import com.jobby.authorization.domain.result.Field;
import com.jobby.authorization.domain.result.Result;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.SerializationException;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static com.jobby.authorization.infraestructure.TestAssertions.*;

@ExtendWith(MockitoExtension.class)
public class RedisCacheServiceTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @InjectMocks
    private RedisCacheService redisCacheService;

    @Test
    public void put_whenKeyAreNull(){
        // Act
        var result = this.redisCacheService.put(null, "hi", Duration.ofDays(10));

        // Assert
        assertFailure(result, ErrorType.VALIDATION_ERROR, "key", "the cache key is required");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  ", "       "})
    public void put_whenKeyIsBlank(String key){
        // Act
        var result = this.redisCacheService.put(key, "hi", Duration.ofDays(10));

        // Assert
        assertFailure(result, ErrorType.VALIDATION_ERROR, "key", "the cache key is required");
    }

    @Test
    public void put_whenThrowsSerializationException(){
        when(this.redisTemplate.opsForValue()).thenThrow(SerializationException.class);

        // Act
        var result = this.redisCacheService.put("thing", "hi", Duration.ofDays(10));

        // Assert
        assertFailure(result, ErrorType.ITN_SERIALIZATION_ERROR, "serialization", "serialization failed, the object provided are invalid to serialize");
    }

    @Test
    public void put_whenThrowsRedisConnectionException(){
        when(this.redisTemplate.opsForValue()).thenThrow(RedisConnectionFailureException.class);

        // Act
        var result = this.redisCacheService.put("thing", "hi", Duration.ofDays(10));

        // Assert
        assertFailure(result, ErrorType.ITN_EXTERNAL_SERVICE_FAILURE, "redis", "Error connection with redis");
    }

    @Test
    public void put_whenAllIsCorrect(){
        // Arrange
        var expectedResult = Result.success(null);

        var valueOperationsMock = mock(ValueOperations.class);
        when(this.redisTemplate.opsForValue()).thenReturn(valueOperationsMock);

        // Act
        var result = this.redisCacheService.put("thing", "hi", Duration.ofDays(10));

        // Assert
        assertSuccess(result);
        assertEquals(expectedResult, result);
    }

    @Test
    public void get_whenKeyIsNull(){
        // Act
        var result = this.redisCacheService.get(null, String.class);

        // Assert
        assertFailure(result, ErrorType.VALIDATION_ERROR, "key", "the cache key is required");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  ", "     "})
    public void get_whenKeyIsBlank(String key){
        // Act
        var result = this.redisCacheService.get(key, String.class);

        // Assert
        assertFailure(result, ErrorType.VALIDATION_ERROR, "key", "the cache key is required");
    }

    @Test
    public void get_whenThrowsSerializationException(){
        when(this.redisTemplate.opsForValue()).thenThrow(SerializationException.class);

        // Act
        var result = this.redisCacheService.get("thing", String.class);

        // Assert
        assertFailure(result, ErrorType.ITN_SERIALIZATION_ERROR, "deserialization", "deserialization failed, the object provided are invalid to deserialize in the specified class");
    }

    @Test
    public void get_whenThrowsRedisConnectionException(){
        when(this.redisTemplate.opsForValue()).thenThrow(RedisConnectionFailureException.class);

        // Act
        var result = this.redisCacheService.get("thing", String.class);

        // Assert
        assertFailure(result, ErrorType.ITN_EXTERNAL_SERVICE_FAILURE, "redis", "Error connection with redis");
    }

    @Test
    public void get_whenTypeIsNotValid(){
        var valueOperationsMock = mock(ValueOperations.class);
        when(this.redisTemplate.opsForValue()).thenReturn(valueOperationsMock);
        when(valueOperationsMock.get(anyString())).thenReturn(42);

        // Act
        var result = this.redisCacheService.get("thing", String.class);

        // Assert
        assertFailure(result, ErrorType.ITN_OPERATION_ERROR, "type cast", "the object is not assignable to type");
    }

    @Test
    public void get_whenAllIsCorrect() {
        // Arrange
        var expectedResult = Result.success("expected result");

        var valueOperationsMock = mock(ValueOperations.class);
        when(this.redisTemplate.opsForValue()).thenReturn(valueOperationsMock);
        when(valueOperationsMock.get(anyString())).thenReturn("expected result");

        // Act
        var result = this.redisCacheService.get("thing", String.class);

        // Assert
        assertSuccess(result);
        assertEquals(expectedResult, result);
    }

    @Test
    public void evict_whenKeyIsNull(){
        // Act
        var result = this.redisCacheService.evict(null);

        // Assert
        assertFailure(result, ErrorType.VALIDATION_ERROR, "key", "the cache key is required");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  ", "     "})
    public void evict_whenKeyIsBlank(String key){
        // Act
        var result = this.redisCacheService.evict(key);

        // Assert
        assertFailure(result, ErrorType.VALIDATION_ERROR, "key", "the cache key is required");
    }

    @Test
    public void evict_whenThrowsRedisConnectionException(){
        doThrow(RedisConnectionFailureException.class).when(this.redisTemplate).delete(anyString());

        // Act
        var result = this.redisCacheService.evict("thing");

        // Assert
        assertFailure(result, ErrorType.ITN_EXTERNAL_SERVICE_FAILURE, "redis", "Error connection with redis");
    }

    @Test
    public void evict_whenAllIsCorrect() {
        // Arrange
        var expectedResult = Result.success(null);

        doReturn(null).when(this.redisTemplate).delete(anyString());

        // Act
        var result = this.redisCacheService.evict("thing");

        assertSuccess(result);
        assertEquals(expectedResult, result);
    }

}
