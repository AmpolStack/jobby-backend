package com.jobby.authorization.infraestructure.adapters.out.tokens;

import com.jobby.authorization.domain.result.ErrorType;
import com.jobby.authorization.domain.result.Field;
import com.jobby.authorization.domain.result.Result;
import com.jobby.authorization.domain.shared.TokenData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Base64;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

public class JwtGeneratorServiceTest {

    private TokenData VALID_TOKEN_DATA;

    private static String VALID_KEY_BASE_64;

    @BeforeAll
    public static void setUpKey(){
        var bytes = new byte[256/8];
        new Random().nextBytes(bytes);
        VALID_KEY_BASE_64 = Base64.getEncoder().encodeToString(bytes);
    }

    @BeforeEach
    public void setUpTokenData(){
        VALID_TOKEN_DATA = new TokenData(
                1,
                "valid@email.com",
                "valid.audience.com",
                "valid.issuer.com",
                "10902",
                6000);
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

    @Test
    public void generate_WhenTokenDataEmailPropertyIsNull(){
        // Arrange
        var expectedResult = Result.failure(ErrorType.VALIDATION_ERROR,
                new Field("tokenData.email",
                        "The email in token data is null")
        );

        VALID_TOKEN_DATA.setEmail(null);

        // Act
        var resp = this.jwtGeneratorService.generate(VALID_TOKEN_DATA, VALID_KEY_BASE_64);

        // Assert
        assertEquals(expectedResult, resp);
        assertTrue(resp.isFailure());
    }
}
