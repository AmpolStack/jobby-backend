package com.jobby.authorization.infraestructure.out;

import com.jobby.authorization.infraestructure.adapters.out.BcryptHashingService;
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
    public void matches_whenEquals(String input){
        // Act
        var hash = this.bcryptHashingService.hash(input);
        var resp = this.bcryptHashingService.matches(input, hash);

        // Assert
        Assertions.assertTrue(resp);
    }

    @ParameterizedTest
    @ValueSource(strings = {"12345678", "password", "my-name+year", ""})
    public void matches_whenNotEquals(String input){
        // Act
        var hash = this.bcryptHashingService.hash(input);

        var bytePadding = new byte[8];
        var random = new Random();
        random.nextBytes(bytePadding);
        var padding = Base64.getEncoder().encodeToString(bytePadding);


        var resp = this.bcryptHashingService.matches(input + padding, hash);

        // Assert
        Assertions.assertFalse(resp);
    }

    @Test
    public void hash_WhenInputAreNull() {
        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.bcryptHashingService.hash(null));
    }

}
