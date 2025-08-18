package com.jobby.authorization.infraestructure.adapters.out.tokens;

import com.jobby.authorization.domain.ports.out.SafeResultValidator;
import com.jobby.authorization.domain.shared.errors.ErrorType;
import com.jobby.authorization.domain.shared.TokenData;
import com.jobby.authorization.domain.shared.errors.Field;
import com.jobby.authorization.domain.shared.result.Result;
import com.jobby.authorization.domain.shared.validators.ValidationChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Base64;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import static com.jobby.authorization.TestAssertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtGeneratorServiceTest {

    private TokenData VALID_TOKEN_DATA;
    private String VALID_TOKEN;
    private String VALID_KEY_BASE_64;

    @Mock
    private SafeResultValidator validator;

    @InjectMocks
    private JwtGeneratorService jwtGeneratorService;

    @BeforeEach
    public void setUpTokenDataAndToken(){
        VALID_TOKEN_DATA = new TokenData(
                1,
                "valid@email.com",
                "valid.audience.com",
                "valid.issuer.com",
                120000);

        var bytes = new byte[256/8];
        new Random().nextBytes(bytes);
        VALID_KEY_BASE_64 = Base64.getEncoder().encodeToString(bytes);

        VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyIiwiaXNzIjoiY29tLmpvYmJ5LmF1dGhvcml6YXRpb24iLCJhdWQiOlsiY29tLmpvYmJ5LmVtcGxveWVlIl0sImNvbS5qb2JieS5lbXBsb3llZS5waG9uZSI6IjM0NDExMzEiLCJpYXQiOjE3NTQ2OTM0NDksImV4cCI6MTc1NDY5MzU2OX0.g1kE3pxqqyh-QorArs1yMLfULmEpBHgz0M4vaLvkOR0";
    }

    @Test
    public void generate_WhenTokenDataIsNull(){
        // Arrange
        var expectedResult = Result.failure(ErrorType.ITS_INVALID_OPTION_PARAMETER,
                new Field("expected-instance", "expected-reason"));

        when(this.validator.validate(any())).thenReturn(Result.renewFailure(expectedResult));

        // Act
        var result = this.jwtGeneratorService.generate(null, VALID_KEY_BASE_64);

        // Assert
        assertFailure(result, Result.renewFailure(expectedResult));
    }

    @Test
    public void generate_WhenKeyIsNull(){
        // Arrange
        when(this.validator.validate(any())).thenReturn(Result.success(null));

        var expectedResult = ValidationChain.create()
                .validateInternalNotBlank(null, "jwt-key")
                .build();

        // Act
        var resp = this.jwtGeneratorService.generate(VALID_TOKEN_DATA, null);

        // Assert
        assertFailure(resp, Result.renewFailure(expectedResult));
    }

    @ParameterizedTest
    @MethodSource("com.jobby.authorization.TestStreams#blankStringList")
    public void generate_WhenKeyIsBlank(String key){
        // Arrange
        when(this.validator.validate(any())).thenReturn(Result.success(null));

        var expectedResult = ValidationChain.create()
                .validateInternalNotBlank(key, "jwt-key")
                .build();

        // Act
        var resp = this.jwtGeneratorService.generate(VALID_TOKEN_DATA, key);

        // Assert
        assertFailure(resp, Result.renewFailure(expectedResult));
    }

    @ParameterizedTest
    @MethodSource("invalidBase64Keys")
    public void generate_WhenKeyIsInvalidInBase64(String key){
        // Arrange
        when(this.validator.validate(any())).thenReturn(Result.success(null));

        var expectedResult = Result.failure(ErrorType.ITS_INVALID_OPTION_PARAMETER,
                new Field("key", "The key is not valid base64"));

        // Act
        var resp = this.jwtGeneratorService.generate(VALID_TOKEN_DATA, key);

        // Assert
        assertFailure(resp, Result.renewFailure(expectedResult));
    }

    @ParameterizedTest
    @MethodSource("invalidKeyBitLengths")
    public void generate_WhenKeyLengthIsInvalid(int keyByteLength){
        // Arrange
        when(this.validator.validate(any())).thenReturn(Result.success(null));

        var bytes = new byte[keyByteLength/8];
        new Random().nextBytes(bytes);
        VALID_KEY_BASE_64 = Base64.getEncoder().encodeToString(bytes);

        var expectedResult = ValidationChain.create()
                .validateInternalAnyMatch(VALID_KEY_BASE_64, new Integer[]{ 256, 384, 512 }, "jwt-length")
                .build();

        // Act
        var resp = this.jwtGeneratorService.generate(VALID_TOKEN_DATA, VALID_KEY_BASE_64);

        // Assert
        assertFailure(resp, Result.renewFailure(expectedResult));
    }

    @RepeatedTest(100)
    public void generate_whenAllIsCorrect(){
        // Act
        when(this.validator.validate(any())).thenReturn(Result.success(null));

        var resp = this.jwtGeneratorService.generate(VALID_TOKEN_DATA, VALID_KEY_BASE_64);

        // Assert
        assertSuccess(resp);
    }


    @Test()
    public void obtainData_whenTokenIsNull(){
        // Arrange
        var expectedResult = ValidationChain.create()
                .validateInternalNotBlank(null, "jwt")
                .build();

        // Act
        var result = this.jwtGeneratorService.obtainData(null, VALID_KEY_BASE_64);

        // Assert
        assertFailure(result, Result.renewFailure(expectedResult));
    }

    @ParameterizedTest
    @MethodSource("com.jobby.authorization.TestStreams#blankStringList")
    public void obtainData_whenTokenIsBlank(String token){
        // Arrange
        var expectedResult = ValidationChain.create()
                .validateInternalNotBlank(token, "jwt")
                .build();

        // Act
        var result = this.jwtGeneratorService.obtainData(token, VALID_KEY_BASE_64);

        // Assert
        assertFailure(result, Result.renewFailure(expectedResult));
    }

    @Test
    public void obtainData_WhenKeyIsNull(){
        // Arrange
        var expectedResult =  ValidationChain.create()
                .validateInternalNotBlank(null, "jwt-key")
                .build();

        // Act
        var resp = this.jwtGeneratorService.obtainData("a", null);

        // Assert
        assertFailure(resp, Result.renewFailure(expectedResult));
    }

    @ParameterizedTest
    @MethodSource("com.jobby.authorization.TestStreams#blankStringList")
    public void obtainData_WhenKeyIsBlank(String key){
        // Arrange
        var expectedResult =  ValidationChain.create()
                .validateInternalNotBlank(key, "jwt-key")
                .build();

        // Act
        var resp = this.jwtGeneratorService.obtainData("a", key);

        // Assert
        assertFailure(resp, Result.renewFailure(expectedResult));
    }

    @ParameterizedTest
    @MethodSource("invalidBase64Keys")
    public void obtainData_WhenKeyIsInvalidInBase64(String key){
        // Arrange
        var expectedResult = Result.failure(ErrorType.ITS_INVALID_OPTION_PARAMETER,
                new Field("key", "The key is not valid base64"));

        // Act
        var resp = this.jwtGeneratorService.obtainData(VALID_TOKEN, key);

        // Assert
        assertFailure(resp, Result.renewFailure(expectedResult));
    }

    @ParameterizedTest
    @MethodSource("invalidKeyBitLengths")
    public void obtainData_WhenKeyLengthIsInvalid(int keyByteLength){
        var bytes = new byte[keyByteLength/8];
        new Random().nextBytes(bytes);
        VALID_KEY_BASE_64 = Base64.getEncoder().encodeToString(bytes);

        var expectedResult = ValidationChain.create()
                .validateInternalAnyMatch(keyByteLength * 8,new Integer[]{ 256, 384, 512 }, "jwt-length")
                .build();
        // Act
        var resp = this.jwtGeneratorService.obtainData(VALID_TOKEN, VALID_KEY_BASE_64);

        // Assert
        assertFailure(resp, Result.renewFailure(expectedResult));
    }

    @RepeatedTest(100)
    public void obtainData_WhenTokenIsInvalid(){
        var bytes = new byte[256/8];
        new Random().nextBytes(bytes);
        VALID_TOKEN = Base64.getEncoder().encodeToString(bytes);

        var expectedResult = Result.failure(ErrorType.ITS_OPERATION_ERROR,
                new Field("jwt", "The provided token is invalid")
        );

        // Act
        var resp = this.jwtGeneratorService.obtainData(VALID_TOKEN, VALID_KEY_BASE_64);

        // Assert
        assertFailure(resp, Result.renewFailure(expectedResult));
    }

    @RepeatedTest(100)
    public void obtainData_whenAllIsCorrect(){
        // Act
        var resp = this.jwtGeneratorService.generate(VALID_TOKEN_DATA, VALID_KEY_BASE_64)
                .flatMap(token -> this.jwtGeneratorService.obtainData(token, VALID_KEY_BASE_64));

        // Assert
        assertSuccess(resp);
    }


    @Test()
    public void isValid_whenTokenIsNull(){
        // Act
        var result = this.jwtGeneratorService.isValid(null, VALID_KEY_BASE_64);

        // Assert
        assertFailure(result, ErrorType.ITS_OPERATION_ERROR, "token", "The provided token is null or blank");
    }

    @ParameterizedTest
    @MethodSource("blankStringsUpTo5")
    public void isValid_whenTokenIsBlank(String token){
        // Act
        var result = this.jwtGeneratorService.isValid(token, VALID_KEY_BASE_64);

        // Assert
        assertFailure(result, ErrorType.ITS_OPERATION_ERROR, "token", "The provided token is null or blank");
    }

    @Test
    public void isValid_WhenKeyIsNull(){
        // Act
        var resp = this.jwtGeneratorService.isValid(VALID_TOKEN, null);

        // Assert
        assertFailure(resp, ErrorType.VALIDATION_ERROR, "key", "The token key is null or empty");
    }

    @ParameterizedTest
    @MethodSource("blankStringsUpTo4")
    public void isValid_WhenKeyIsBlank(String key){
        // Act
        var resp = this.jwtGeneratorService.isValid(VALID_TOKEN, key);

        // Assert
        assertFailure(resp, ErrorType.VALIDATION_ERROR, "key", "The token key is null or empty");
    }

    @ParameterizedTest
    @MethodSource("invalidBase64Keys")
    public void isValid_WhenKeyIsInvalidInBase64(String key){
        // Act
        var resp = this.jwtGeneratorService.isValid(VALID_TOKEN, key);

        // Assert
        assertFailure(resp, ErrorType.ITS_INVALID_OPTION_PARAMETER, "key", "The key is not valid base64");
    }

    @ParameterizedTest
    @MethodSource("invalidKeyBitLengths")
    public void isValid_WhenKeyLengthIsInvalid(int keyByteLength){
        var bytes = new byte[keyByteLength/8];
        new Random().nextBytes(bytes);
        VALID_KEY_BASE_64 = Base64.getEncoder().encodeToString(bytes);

        // Act
        var resp = this.jwtGeneratorService.isValid(VALID_TOKEN, VALID_KEY_BASE_64);

        // Assert
        assertFailure(resp, ErrorType.ITS_INVALID_OPTION_PARAMETER, "key", "The key length are invalid");
    }

    @RepeatedTest(100)
    public void isValid_WhenTokenIsInvalid(){
        var bytes = new byte[256/8];
        new Random().nextBytes(bytes);
        VALID_TOKEN = Base64.getEncoder().encodeToString(bytes);

        // Act
        var resp = this.jwtGeneratorService.isValid(VALID_TOKEN, VALID_KEY_BASE_64);

        // Assert
        assertFailure(resp, ErrorType.ITS_OPERATION_ERROR, "token", "The provided token is invalid");
    }

    @RepeatedTest(100)
    public void isValid_whenAllIsCorrect(){
        // Act
        var resp = this.jwtGeneratorService.generate(VALID_TOKEN_DATA, VALID_KEY_BASE_64)
                .flatMap(token -> this.jwtGeneratorService.isValid(token, VALID_KEY_BASE_64));

        // Assert
        assertSuccess(resp);
    }

    private static IntStream negativeOrZeroExpirationTimes() {
        return IntStream.of(-100, -1, 0, -2000, -5);
    }

    private static Stream<String> invalidBase64Keys() {
        return Stream.of("invalidBase64", "textNormal%", "amp%", "lowest%", "hardest%");
    }

    private static Stream<String> blankStringsUpTo4() {
        return Stream.of("", " ", "  ", "   ", "    ");
    }

    private static Stream<String> blankStringsUpTo5() {
        return Stream.of("", " ", "  ", "   ", "     ");
    }

    private static IntStream invalidKeyBitLengths() {
        return IntStream.of(100, 255, 264, 383, 392, 511, 520);
    }

}
