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
}
