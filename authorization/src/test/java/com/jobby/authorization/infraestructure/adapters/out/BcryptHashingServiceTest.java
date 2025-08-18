package com.jobby.authorization.infraestructure.adapters.out;

import com.jobby.authorization.domain.shared.errors.Error;
import com.jobby.authorization.domain.shared.errors.ErrorType;
import com.jobby.authorization.domain.shared.result.Result;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
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
        var hashResult = this.bcryptHashingService.hash(input)
                .flatMap( hash -> {
                    var bytePadding = new byte[8];
                    var random = new Random();
                    random.nextBytes(bytePadding);
                    var padding = Base64.getEncoder().encodeToString(bytePadding);
                    return this.bcryptHashingService.matches(input + padding, hash);
                });

        // Assert
        assertTrue(hashResult.isSuccess());
        assertFalse(hashResult.getData());
        assertEquals(expectedResult, hashResult);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "\n", "\r", "\r\n", " "})
    public void hash_WhenInputAreBlank(String input) {
        // Act
        var hashResult = this.bcryptHashingService.hash(input);

        // Assert
        assertFailure(hashResult, ErrorType.VALIDATION_ERROR, "hash-input", "hash-input is blank");
    }

    @Test
    public void hash_WhenInputIsNull() {
        // Act
        var hashResult = this.bcryptHashingService.hash(null);


        // Assert
        assertFailure(hashResult, ErrorType.VALIDATION_ERROR, "hash-input", "hash-input is null");
    }

    @RepeatedTest(value = 100, name = "{displayName} - rep#{currentRepetition}")
    public void hash_WhenInputExceeds72Bytes(RepetitionInfo info) {
        //Arrange
        var input = "x".repeat(info.getCurrentRepetition() + 72);

        // Act
        var hashResult = this.bcryptHashingService.hash(input);

        // Assert
        assertFailure(hashResult, ErrorType.VALIDATION_ERROR, "hash-input-bytes", "hash-input-bytes is bigger than 72");
    }


    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "\n", "\r", "\r\n", " "})
    public void matches_WhenInputAreBlank_FirstInput(String input) {
        // Act
        var hashResult = this.bcryptHashingService.matches(input, "exampleHash");

        // Assert
        assertFailure(hashResult, ErrorType.VALIDATION_ERROR, "plain-input", "plain-input is blank");
        assertNull(hashResult.getData());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "\n", "\r", "\r\n", " "})
    public void matches_WhenInputAreBlank_SecondInput(String input) {
        // Act
        var hashResult = this.bcryptHashingService.matches("examplePlain", input);

        // Assert
        assertFailure(hashResult, ErrorType.VALIDATION_ERROR, "hash-input", "hash-input is blank");
        assertNull(hashResult.getData());
    }

    @Test
    public void matches_WhenInputAreNull_FirstInput() {
        // Act
        var hashResult = this.bcryptHashingService.matches(null, "exampleHash");

        // Assert
        assertFailure(hashResult, ErrorType.VALIDATION_ERROR, "plain-input", "plain-input is null");
        assertNull(hashResult.getData());
    }

    @Test
    public void matches_WhenInputAreNull_SecondInput() {
        // Act
        var hashResult = this.bcryptHashingService.matches("examplePlain", null);

        // Assert
        assertFailure(hashResult, ErrorType.VALIDATION_ERROR, "hash-input", "hash-input is null");
        assertNull(hashResult.getData());
    }
}
