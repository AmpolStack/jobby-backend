package com.jobby.authorization.infraestructure.adapters.out.tokens;

import com.jobby.authorization.domain.result.ErrorType;
import com.jobby.authorization.domain.result.Field;
import com.jobby.authorization.domain.result.Result;
import com.jobby.authorization.domain.shared.TokenData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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

    @ParameterizedTest
    @ValueSource(ints = {-100, -1, 0, -2000, -5})
    public void generate_WhenTokenDataExpirationTimeIsZeroOrNegative(int msExpirationTime){
        // Arrange
        var expectedResult = Result.failure(ErrorType.VALIDATION_ERROR,
                new Field("tokenData.msExpirationTime",
                        "The Expiration time in token data is less than 0")
        );

        VALID_TOKEN_DATA.setMsExpirationTime(msExpirationTime);

        // Act
        var resp = this.jwtGeneratorService.generate(VALID_TOKEN_DATA, VALID_KEY_BASE_64);

        // Assert
        assertEquals(expectedResult, resp);
        assertTrue(resp.isFailure());
    }

     @Test
     public void generate_WhenTokenDataPhoneWhenIsNull(){
         // Arrange
         var expectedResult = Result.failure(ErrorType.VALIDATION_ERROR,
                 new Field("tokenData.phone",
                         "The phone in token data is null")
         );

         VALID_TOKEN_DATA.setPhone(null);

         // Act
         var resp = this.jwtGeneratorService.generate(VALID_TOKEN_DATA, VALID_KEY_BASE_64);

         // Assert
         assertEquals(expectedResult, resp);
         assertTrue(resp.isFailure());
     }

    @Test
    public void generate_WhenKeyIsNull(){
        // Arrange
        var expectedResult = Result.failure(ErrorType.VALIDATION_ERROR,
                new Field("key",
                        "The token key is null or empty")
        );

        // Act
        var resp = this.jwtGeneratorService.generate(VALID_TOKEN_DATA, null);

        // Assert
        assertEquals(expectedResult, resp);
        assertTrue(resp.isFailure());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  ", "   ", "    "})
    public void generate_WhenKeyIsBlank(String key){
        // Arrange
        var expectedResult = Result.failure(ErrorType.VALIDATION_ERROR,
                new Field("key",
                        "The token key is null or empty")
        );

        // Act
        var resp = this.jwtGeneratorService.generate(VALID_TOKEN_DATA, key);

        // Assert
        assertEquals(expectedResult, resp);
        assertTrue(resp.isFailure());
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalidBase64", "textNormal%", "amp%", "lowest%", "hardest%"})
    public void generate_WhenKeyIsInvalidInBase64(String key){
        // Arrange
        var expectedResult = Result.failure(ErrorType.ITN_INVALID_OPTION_PARAMETER,
                new Field("key", "The key is not valid base64"));

        // Act
        var resp = this.jwtGeneratorService.generate(VALID_TOKEN_DATA, key);

        // Assert
        assertEquals(expectedResult, resp);
        assertTrue(resp.isFailure());
    }

    @RepeatedTest(1)
    public void generate_WhenKeyLengthIsInvalid(){
        // Arrange
        var expectedResult = Result.failure(ErrorType.ITN_INVALID_OPTION_PARAMETER,
                new Field("key",
                        "The key length are invalid")
        );

        var bytes = new byte[255/8];
        new Random().nextBytes(bytes);
        VALID_KEY_BASE_64 = Base64.getEncoder().encodeToString(bytes);

        // Act
        var resp = this.jwtGeneratorService.generate(VALID_TOKEN_DATA, VALID_KEY_BASE_64);

        // Assert
        assertEquals(expectedResult, resp);
        assertTrue(resp.isFailure());
    }





}
