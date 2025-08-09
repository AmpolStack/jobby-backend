package com.jobby.authorization.infraestructure.adapters.out.tokens;

import org.junit.jupiter.api.RepeatedTest;
import static org.junit.jupiter.api.Assertions.*;

public class DefaultRefreshTokenGeneratorServiceTest {

    private static final DefaultRefreshTokenGeneratorService defaultRefreshTokenGeneratorService = new DefaultRefreshTokenGeneratorService();

    @RepeatedTest(100)
    public void generate_whenAllIsCorrect() {
        // Act
        var result = defaultRefreshTokenGeneratorService.generate();

        // Assert
        assertTrue(result.isSuccess());
    }
}
