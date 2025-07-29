package com.jobby.authorization.infraestructure.adapters.out;

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
        // Act
        var matchesResult = this.bcryptHashingService.hash(input)
                .flatMap((hash) -> this.bcryptHashingService.matches(input, hash));

        // Assert
        Assertions.assertTrue(matchesResult.isSuccess());
        Assertions.assertTrue(matchesResult.getData());
    }

    @ParameterizedTest
    @ValueSource(strings = {"12345678", "password", "my-name+year", "a"})
    public void matches_whenNotEquals(String input){
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
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "\n", "\r", "\r\n", " "})
    public void hash_WhenInputAreBlank(String input) {
        // Act
        var hashResult = this.bcryptHashingService.hash(input);

        // Assert
        Assertions.assertFalse(hashResult.isSuccess());
    }

    @Test
    public void hash_WhenInputIsNull() {
        // Act
        var hashResult = this.bcryptHashingService.hash(null);

        // Assert
        Assertions.assertFalse(hashResult.isSuccess());
    }


    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "\n", "\r", "\r\n", " "})
    public void matches_WhenInputAreBlank(String input) {
        // Act

        var hashResult = this.bcryptHashingService.matches(input, input);

        // Assert
        Assertions.assertFalse(hashResult.isSuccess());
        Assertions.assertNull(hashResult.getData());
    }

    @Test
    public void matches_WhenInputAreNull() {
        // Act
        var hashResult = this.bcryptHashingService.matches(null, null);

        // Assert
        Assertions.assertFalse(hashResult.isSuccess());
        Assertions.assertNull(hashResult.getData());
    }

}
