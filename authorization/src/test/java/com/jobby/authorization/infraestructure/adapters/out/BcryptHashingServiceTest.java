package com.jobby.authorization.infraestructure.adapters.out;

import com.jobby.authorization.domain.result.Error;
import com.jobby.authorization.domain.result.ErrorType;
import com.jobby.authorization.domain.result.Field;
import com.jobby.authorization.domain.result.Result;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.Base64;
import java.util.Random;

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
        Assertions.assertTrue(matchesResult.isSuccess());
        Assertions.assertTrue(matchesResult.getData());
        Assertions.assertEquals(expectedResult, matchesResult);

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
        Assertions.assertTrue(hashResult.isSuccess());
        Assertions.assertFalse(hashResult.getData());
        Assertions.assertEquals(expectedResult, hashResult);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "\n", "\r", "\r\n", " "})
    public void hash_WhenInputAreBlank(String input) {
        // Arrange
        Result<String, Error> expectedResult = Result.failure(
                ErrorType.VALIDATION_ERROR,
                new Field(
                        "input",
                        "the input are null or blank"
                )
        );

        // Act
        var hashResult = this.bcryptHashingService.hash(input);

        // Assert
        Assertions.assertFalse(hashResult.isSuccess());
        Assertions.assertEquals(expectedResult, hashResult);
    }

    @Test
    public void hash_WhenInputIsNull() {
        // Arrange
        Result<String, Error> expectedResult = Result.failure(
                ErrorType.VALIDATION_ERROR,
                new Field(
                        "input",
                        "the input are null or blank"
                )
        );

        // Act
        var hashResult = this.bcryptHashingService.hash(null);


        // Assert
        Assertions.assertFalse(hashResult.isSuccess());
        Assertions.assertEquals(hashResult, expectedResult);
    }

    @Test
    public void hash_WhenInputExceeds72Bytes() {
        //Arrange
        var bytes = new byte[73];
        new Random().nextBytes(bytes);
        var input = new String(bytes);

        Result<String, Error> expectedResult = Result.failure(
                ErrorType.VALIDATION_ERROR,
                new Field(
                        "input",
                        "the input are invalid, because exceeds the 72 bytes of length"
                )
        );

        // Act
        var hashResult = this.bcryptHashingService.hash(input);

        // Assert
        Assertions.assertFalse(hashResult.isSuccess());
        Assertions.assertEquals(hashResult, expectedResult);
    }


    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "\n", "\r", "\r\n", " "})
    public void matches_WhenInputAreBlank_FirstInput(String input) {
        // Arrange
        Result<Boolean, Error> expectedOne = Result.failure(
                ErrorType.VALIDATION_ERROR,
                new Field(
                        "plain",
                        "The input are null or blank"
                )
        );

        // Act
        var hashResult = this.bcryptHashingService.matches(input, "exampleHash");

        // Assert
        Assertions.assertFalse(hashResult.isSuccess());
        Assertions.assertNull(hashResult.getData());
        Assertions.assertEquals(expectedOne, hashResult);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "\n", "\r", "\r\n", " "})
    public void matches_WhenInputAreBlank_SecondInput(String input) {
        // Arrange
        Result<Boolean, Error> expectedTwo = Result.failure(
                ErrorType.VALIDATION_ERROR,
                new Field(
                        "hash",
                        "The input are null or blank"
                )
        );

        // Act
        var hashResult = this.bcryptHashingService.matches("examplePlain", input);

        // Assert
        Assertions.assertFalse(hashResult.isSuccess());
        Assertions.assertNull(hashResult.getData());
        Assertions.assertEquals(expectedTwo, hashResult);
    }

    @Test
    public void matches_WhenInputAreNull_FirstInput() {
        // Arrange
        Result<Boolean, Error> expectedOne = Result.failure(
                ErrorType.VALIDATION_ERROR,
                new Field(
                        "plain",
                        "The input are null or blank"
                )
        );

        // Act
        var hashResult = this.bcryptHashingService.matches(null, "exampleHash");

        // Assert
        Assertions.assertFalse(hashResult.isSuccess());
        Assertions.assertNull(hashResult.getData());
        Assertions.assertEquals(expectedOne, hashResult);
    }

    @Test
    public void matches_WhenInputAreNull_SecondInput() {
        // Arrange
        Result<Boolean, Error> expectedTwo = Result.failure(
                ErrorType.VALIDATION_ERROR,
                new Field(
                        "hash",
                        "The input are null or blank"
                )
        );

        // Act
        var hashResult = this.bcryptHashingService.matches("examplePlain", null);

        // Assert
        Assertions.assertFalse(hashResult.isSuccess());
        Assertions.assertNull(hashResult.getData());
        Assertions.assertEquals(expectedTwo, hashResult);
    }
}
