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

@ExtendWith(MockitoExtension.class)
public class RedisCacheServiceTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @InjectMocks
    private RedisCacheService redisCacheService;

    @Test
    public void put_whenKeyAreNull(){
        // Arrange
        var expectedResult = Result.failure(
                ErrorType.VALIDATION_ERROR,
                new Field(
                        "key",
                        "the cache key is required"
                )
        );

        // Act
        var result = this.redisCacheService.put(null, "hi", Duration.ofDays(10));

        // Assert
        assertTrue(result.isFailure());
        assertEquals(expectedResult, result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  ", "       "})
    public void put_whenKeyIsBlank(String key){
        // Arrange
        var expectedResult = Result.failure(
                ErrorType.VALIDATION_ERROR,
                new Field(
                        "key",
                        "the cache key is required"
                )
        );

        // Act
        var result = this.redisCacheService.put(key, "hi", Duration.ofDays(10));

        // Assert
        assertTrue(result.isFailure());
        assertEquals(expectedResult, result);
    }

    @Test
    public void put_whenThrowsSerializationException(){
        // Arrange
        var expectedResult = Result.failure(
                ErrorType.ITN_SERIALIZATION_ERROR,
                new Field(
                        "serialization",
                        "serialization failed, the object provided are invalid to serialize"
                )
        );

        when(this.redisTemplate.opsForValue()).thenThrow(SerializationException.class);

        // Act
        var result = this.redisCacheService.put("thing", "hi", Duration.ofDays(10));

        // Assert
        assertTrue(result.isFailure());
        assertEquals(expectedResult, result);
    }

    @Test
    public void put_whenThrowsRedisConnectionException(){
        // Arrange
        var expectedResult = Result.failure(
                ErrorType.ITN_EXTERNAL_SERVICE_FAILURE,
                new Field(
                        "redis",
                        "Error connection with redis"
                )
        );

        when(this.redisTemplate.opsForValue()).thenThrow(RedisConnectionFailureException.class);

        // Act
        var result = this.redisCacheService.put("thing", "hi", Duration.ofDays(10));

        // Assert
        assertTrue(result.isFailure());
        assertEquals(expectedResult, result);
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
        assertTrue(result.isSuccess());
        assertEquals(expectedResult, result);
    }

    @Test
    public void get_whenKeyIsNull(){
        // Arrange
        var expectedResult = Result.failure(
                ErrorType.VALIDATION_ERROR,
                new Field(
                        "key",
                        "the cache key is required"
                )
        );

        // Act
        var result = this.redisCacheService.get(null, String.class);

        // Assert
        assertTrue(result.isFailure());
        assertEquals(expectedResult, result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  ", "     "})
    public void get_whenKeyIsBlank(String key){
        // Arrange
        var expectedResult = Result.failure(
                ErrorType.VALIDATION_ERROR,
                new Field(
                        "key",
                        "the cache key is required"
                )
        );

        // Act
        var result = this.redisCacheService.get(key, String.class);

        // Assert
        assertTrue(result.isFailure());
        assertEquals(expectedResult, result);
    }

    @Test
    public void get_whenThrowsSerializationException(){
        // Arrange
        var expectedResult = Result.failure(ErrorType.ITN_SERIALIZATION_ERROR,
                new Field(
                        "deserialization",
                        "deserialization failed, the object provided are invalid to deserialize in the specified class"
                )
        );

        when(this.redisTemplate.opsForValue()).thenThrow(SerializationException.class);

        // Act
        var result = this.redisCacheService.get("thing", String.class);

        // Assert
        assertTrue(result.isFailure());
        assertEquals(expectedResult, result);
    }

    @Test
    public void get_whenThrowsRedisConnectionException(){
        // Arrange
        var expectedResult = Result.failure(
                ErrorType.ITN_EXTERNAL_SERVICE_FAILURE,
                new Field(
                        "redis",
                        "Error connection with redis"
                )
        );

        when(this.redisTemplate.opsForValue()).thenThrow(RedisConnectionFailureException.class);

        // Act
        var result = this.redisCacheService.get("thing", String.class);

        // Assert
        assertTrue(result.isFailure());
        assertEquals(expectedResult, result);
    }

    @Test
    public void get_whenTypeIsNotValid(){
        // Arrange
        var expectedResult = Result.failure(
                ErrorType.ITN_OPERATION_ERROR,
                new Field(
                        "type cast",
                        "the object is not assignable to type"
                )
        );

        var valueOperationsMock = mock(ValueOperations.class);
        when(this.redisTemplate.opsForValue()).thenReturn(valueOperationsMock);
        when(valueOperationsMock.get(anyString())).thenReturn(42);

        // Act
        var result = this.redisCacheService.get("thing", String.class);

        // Assert
        assertTrue(result.isFailure());
        assertEquals(expectedResult, result);
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
        assertTrue(result.isSuccess());
        assertEquals(expectedResult, result);
    }

}
