package com.jobby.authorization.infraestructure.adapters.out.tokens;

import com.jobby.authorization.domain.result.ErrorType;
import com.jobby.authorization.domain.shared.TokenData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.Base64;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import static com.jobby.authorization.infraestructure.TestAssertions.*;

public class JwtGeneratorServiceTest {

    private TokenData VALID_TOKEN_DATA;
    private String VALID_TOKEN;
    private String VALID_KEY_BASE_64;

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

    private final JwtGeneratorService jwtGeneratorService = new JwtGeneratorService();

    @Test
    public void generate_WhenTokenDataIsNull(){
        // Act
        var resp = this.jwtGeneratorService.generate(null, VALID_KEY_BASE_64);

        // Assert
        assertFailure(resp, ErrorType.ITN_OPERATION_ERROR, "tokenData", "The provided token data is null");
    }

    @Test
    public void generate_WhenTokenDataEmailPropertyIsNull(){
        VALID_TOKEN_DATA.setEmail(null);

        // Act
        var resp = this.jwtGeneratorService.generate(VALID_TOKEN_DATA, VALID_KEY_BASE_64);

        // Assert
        assertFailure(resp, ErrorType.VALIDATION_ERROR, "tokenData.email", "The email in token data is null");
    }

    @ParameterizedTest
    @MethodSource("negativeOrZeroExpirationTimes")
    public void generate_WhenTokenDataExpirationTimeIsZeroOrNegative(int msExpirationTime){
        VALID_TOKEN_DATA.setMsExpirationTime(msExpirationTime);

        // Act
        var resp = this.jwtGeneratorService.generate(VALID_TOKEN_DATA, VALID_KEY_BASE_64);

        // Assert
        assertFailure(resp, ErrorType.VALIDATION_ERROR, "tokenData.msExpirationTime", "The Expiration time in token data is less than 0");
    }

    @Test
    public void generate_WhenKeyIsNull(){
        // Act
        var resp = this.jwtGeneratorService.generate(VALID_TOKEN_DATA, null);

        // Assert
        assertFailure(resp, ErrorType.VALIDATION_ERROR, "key", "The token key is null or empty");
    }

    @ParameterizedTest
    @MethodSource("blankStringsUpTo4")
    public void generate_WhenKeyIsBlank(String key){
        // Act
        var resp = this.jwtGeneratorService.generate(VALID_TOKEN_DATA, key);

        // Assert
        assertFailure(resp, ErrorType.VALIDATION_ERROR, "key", "The token key is null or empty");
    }

    @ParameterizedTest
    @MethodSource("invalidBase64Keys")
    public void generate_WhenKeyIsInvalidInBase64(String key){
        // Act
        var resp = this.jwtGeneratorService.generate(VALID_TOKEN_DATA, key);

        // Assert
        assertFailure(resp, ErrorType.ITN_INVALID_OPTION_PARAMETER, "key", "The key is not valid base64");
    }

    @ParameterizedTest
    @MethodSource("invalidKeyBitLengths")
    public void generate_WhenKeyLengthIsInvalid(int keyByteLength){
        var bytes = new byte[keyByteLength/8];
        new Random().nextBytes(bytes);
        VALID_KEY_BASE_64 = Base64.getEncoder().encodeToString(bytes);

        // Act
        var resp = this.jwtGeneratorService.generate(VALID_TOKEN_DATA, VALID_KEY_BASE_64);

        // Assert
        assertFailure(resp, ErrorType.ITN_INVALID_OPTION_PARAMETER, "key", "The key length are invalid");
    }

    @RepeatedTest(100)
    public void generate_whenAllIsCorrect(){
        // Act
        var resp = this.jwtGeneratorService.generate(VALID_TOKEN_DATA, VALID_KEY_BASE_64);

        // Assert
        assertSuccess(resp);
    }


    @Test()
    public void obtainData_whenTokenIsNull(){
        // Act
        var result = this.jwtGeneratorService.obtainData(null, VALID_KEY_BASE_64);

        // Assert
        assertFailure(result, ErrorType.ITN_OPERATION_ERROR, "token", "The provided token is null or blank");
    }

    @ParameterizedTest
    @MethodSource("blankStringsUpTo5")
    public void obtainData_whenTokenIsBlank(String token){
        // Act
        var result = this.jwtGeneratorService.obtainData(token, VALID_KEY_BASE_64);

        // Assert
        assertFailure(result, ErrorType.ITN_OPERATION_ERROR, "token", "The provided token is null or blank");
    }

    @Test
    public void obtainData_WhenKeyIsNull(){
        // Act
        var resp = this.jwtGeneratorService.obtainData("a", null);

        // Assert
        assertFailure(resp, ErrorType.VALIDATION_ERROR, "key", "The token key is null or empty");
    }

    @ParameterizedTest
    @MethodSource("blankStringsUpTo4")
    public void obtainData_WhenKeyIsBlank(String key){
        // Act
        var resp = this.jwtGeneratorService.obtainData(VALID_TOKEN, key);

        // Assert
        assertFailure(resp, ErrorType.VALIDATION_ERROR, "key", "The token key is null or empty");
    }

    @ParameterizedTest
    @MethodSource("invalidBase64Keys")
    public void obtainData_WhenKeyIsInvalidInBase64(String key){
        // Act
        var resp = this.jwtGeneratorService.obtainData(VALID_TOKEN, key);

        // Assert
        assertFailure(resp, ErrorType.ITN_INVALID_OPTION_PARAMETER, "key", "The key is not valid base64");
    }

    @ParameterizedTest
    @MethodSource("invalidKeyBitLengths")
    public void obtainData_WhenKeyLengthIsInvalid(int keyByteLength){
        var bytes = new byte[keyByteLength/8];
        new Random().nextBytes(bytes);
        VALID_KEY_BASE_64 = Base64.getEncoder().encodeToString(bytes);

        // Act
        var resp = this.jwtGeneratorService.obtainData(VALID_TOKEN, VALID_KEY_BASE_64);

        // Assert
        assertFailure(resp, ErrorType.ITN_INVALID_OPTION_PARAMETER, "key", "The key length are invalid");
    }

    @RepeatedTest(100)
    public void obtainData_WhenTokenIsInvalid(){
        var bytes = new byte[256/8];
        new Random().nextBytes(bytes);
        VALID_TOKEN = Base64.getEncoder().encodeToString(bytes);

        // Act
        var resp = this.jwtGeneratorService.obtainData(VALID_TOKEN, VALID_KEY_BASE_64);

        // Assert
        assertFailure(resp, ErrorType.ITN_OPERATION_ERROR, "token", "The provided token is invalid");
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
        assertFailure(result, ErrorType.ITN_OPERATION_ERROR, "token", "The provided token is null or blank");
    }

    @ParameterizedTest
    @MethodSource("blankStringsUpTo5")
    public void isValid_whenTokenIsBlank(String token){
        // Act
        var result = this.jwtGeneratorService.isValid(token, VALID_KEY_BASE_64);

        // Assert
        assertFailure(result, ErrorType.ITN_OPERATION_ERROR, "token", "The provided token is null or blank");
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
        assertFailure(resp, ErrorType.ITN_INVALID_OPTION_PARAMETER, "key", "The key is not valid base64");
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
        assertFailure(resp, ErrorType.ITN_INVALID_OPTION_PARAMETER, "key", "The key length are invalid");
    }

    @RepeatedTest(100)
    public void isValid_WhenTokenIsInvalid(){
        var bytes = new byte[256/8];
        new Random().nextBytes(bytes);
        VALID_TOKEN = Base64.getEncoder().encodeToString(bytes);

        // Act
        var resp = this.jwtGeneratorService.isValid(VALID_TOKEN, VALID_KEY_BASE_64);

        // Assert
        assertFailure(resp, ErrorType.ITN_OPERATION_ERROR, "token", "The provided token is invalid");
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
