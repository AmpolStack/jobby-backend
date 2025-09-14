package com.jobby.authorization.infraestructure.adapters.out;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import com.jobby.domain.mobility.validator.ValidationChain;
import com.jobby.infraestructure.adapter.BcryptHashingService;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.Base64;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;
import static com.jobby.authorization.TestAssertions.*;

public class BcryptHashingServiceTest {

    private final BcryptHashingService bcryptHashingService = new BcryptHashingService();

    @ParameterizedTest
    @ValueSource(strings = {"12345678", "password", "my-name+year"})
    public void matches_whenEquals(String input) {
        // Arrange
        Result<Boolean, Error> expectedResult =  Result.success(true);

        // Act
        var matchesResult = this.bcryptHashingService.hash(input)
                .flatMap((hash) -> this.bcryptHashingService.matches(input, hash));

        // Assert
        assertTrue(matchesResult.isSuccess());
        assertTrue(matchesResult.getData());
        assertEquals(expectedResult, matchesResult);
    }

    @ParameterizedTest
    @ValueSource(strings = {"12345678", "password", "my-name+year", "a"})
    public void matches_whenNotEquals(String input){
        // Arrange
        Result<Boolean, Error> expectedResult =  Result.success(false);

        // Act
        var result = this.bcryptHashingService.hash(input)
                .flatMap( hash -> {
                    var bytePadding = new byte[8];
                    var random = new Random();
                    random.nextBytes(bytePadding);
                    var padding = Base64.getEncoder().encodeToString(bytePadding);
                    return this.bcryptHashingService.matches(input + padding, hash);
                });

        // Assert
        assertTrue(result.isSuccess());
        assertFalse(result.getData());
        assertEquals(expectedResult, result);
    }

    @ParameterizedTest
    @MethodSource("com.jobby.authorization.TestStreams#blankStringList")
    public void hash_WhenInputAreBlank(String input) {
        // Arrange
        var expectedResult = ValidationChain.create()
                .validateInternalNotBlank(input, "hash-input")
                .build();

        // Act
        var result = this.bcryptHashingService.hash(input);

        // Assert
        assertFailure(result, Result.renewFailure(expectedResult));
    }

    @Test
    public void hash_WhenInputIsNull() {
        // Arrange
        var expectedResult = ValidationChain.create()
                .validateInternalNotBlank(null, "hash-input")
                .build();

        // Act
        var result = this.bcryptHashingService.hash(null);

        // Assert
        assertFailure(result, Result.renewFailure(expectedResult));
    }

    @RepeatedTest(value = 100, name = "{displayName} - rep#{currentRepetition}")
    public void hash_WhenInputExceeds72Bytes(RepetitionInfo info) {
        //Arrange
        var repetition = info.getCurrentRepetition() + 72;

        var input = "x".repeat(repetition);

        var expectedResult = ValidationChain.create()
                .validateInternalSmallerThan(
                        repetition,
                        72,
                        "hash-input-bytes")
                .build();
        // Act
        var hashResult = this.bcryptHashingService.hash(input);

        // Assert
        assertFailure(hashResult, Result.renewFailure(expectedResult));
    }


    @ParameterizedTest
    @MethodSource("com.jobby.authorization.TestStreams#blankStringList")
    public void matches_WhenInputAreBlank_FirstInput(String input) {
        // Arrange
        var expectedResult = ValidationChain.create()
                .validateInternalNotBlank(input, "plain-input")
                .build();

        // Act
        var result = this.bcryptHashingService.matches(input, "exampleHash");

        // Assert
        assertFailure(result, Result.renewFailure(expectedResult));
    }

    @ParameterizedTest
    @MethodSource("com.jobby.authorization.TestStreams#blankStringList")
    public void matches_WhenInputAreBlank_SecondInput(String input) {
        // Arrange
        var expectedResult = ValidationChain.create()
                .validateInternalNotBlank(input, "hash-input")
                .build();

        // Act
        var result = this.bcryptHashingService.matches("example-input", input);

        // Assert
        assertFailure(result, Result.renewFailure(expectedResult));
    }

    @Test
    public void matches_WhenInputAreNull_FirstInput() {
        // Arrange
        var expectedResult = ValidationChain.create()
                .validateInternalNotBlank(null, "plain-input")
                .build();

        // Act
        var result = this.bcryptHashingService.matches(null, "exampleHash");

        // Assert
        assertFailure(result, Result.renewFailure(expectedResult));
    }

    @Test
    public void matches_WhenInputAreNull_SecondInput() {
        // Arrange
        var expectedResult = ValidationChain.create()
                .validateInternalNotBlank(null, "hash-input")
                .build();

        // Act
        var result = this.bcryptHashingService.matches("exampleHash", null);

        // Assert
        assertFailure(result, Result.renewFailure(expectedResult));
    }
}
