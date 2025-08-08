package com.jobby.authorization.infraestructure.adapters.out.tokens;

import com.jobby.authorization.domain.result.ErrorType;
import com.jobby.authorization.domain.result.Field;
import com.jobby.authorization.domain.result.Result;
import com.jobby.authorization.domain.shared.TokenData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Base64;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class JwtGeneratorServiceTest {

    private static final TokenData VALID_TOKEN_DATA = new TokenData(
            1,
            "valid@email.com",
            "valid.audience.com",
            "valid.issuer.com",
            "10902",
            6000);

    private static String VALID_KEY_BASE_64;

    @BeforeAll
    public static void setUp(){
        var bytes = new byte[256/8];
        new Random().nextBytes(bytes);
        VALID_KEY_BASE_64 = Base64.getEncoder().encodeToString(bytes);
    }

    private final JwtGeneratorService jwtGeneratorService = new JwtGeneratorService();

    @Test
    public void generate_WhenTokenDataIsNull(){
        // Arrange
        var expectedResult = Result.failure(ErrorType.ITN_OPERATION_ERROR,
                new Field("tokenData",
                        "The provided token data is null")
        );

        // Act
        var resp = this.jwtGeneratorService.generate(null, VALID_KEY_BASE_64);

        // Assert
        assertEquals(expectedResult, resp);
        assertTrue(resp.isFailure());
    }
}
