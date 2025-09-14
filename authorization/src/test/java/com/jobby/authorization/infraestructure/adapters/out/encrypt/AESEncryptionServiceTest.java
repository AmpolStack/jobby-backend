package com.jobby.authorization.infraestructure.adapters.out.encrypt;

import com.jobby.domain.ports.SafeResultValidator;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.error.ErrorType;
import com.jobby.domain.mobility.error.Field;
import com.jobby.domain.mobility.result.Result;
import com.jobby.domain.mobility.validator.ValidationChain;
import com.jobby.domain.configurations.EncryptConfig;
import com.jobby.domain.configurations.EncryptConfig.Iv;
import com.jobby.infraestructure.adapter.encrypt.AESEncryptionService;
import com.jobby.infraestructure.adapter.encrypt.DefaultEncryptBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import javax.crypto.Cipher;
import java.util.Base64;
import java.util.Random;

import static com.jobby.authorization.TestAssertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AESEncryptionServiceTest {

    private final static String VALID_KEY = "p652zw20jx/Bvg/4I7Mrdg==";
    private final static int VALID_IV_LENGTH = 12;
    private final static int VALID_T_LENGTH = 128;
    private final static String VALID_DATA = "Hello World";
    private static EncryptConfig VALID_CONFIG;
    private static final String VALID_CIPHER = "vzBlcYpcMSsdCdYCAAA=";

    @Mock
    private SafeResultValidator validator;

    @Mock
    private DefaultEncryptBuilder defaultEncryptBuilder;

    @InjectMocks
    private AESEncryptionService aesEncryptionService;

    @BeforeEach
    public void setUp() {
        VALID_CONFIG = new EncryptConfig(VALID_KEY, new Iv(VALID_IV_LENGTH, VALID_T_LENGTH));
    }

    @Test
    public void encrypt_whenTheConfigValidationReturnsFailed(){
        // Arrange
        var expectedResult = Result.failure(ErrorType.VALIDATION_ERROR, new Field("expected-instance", "expected-description"));
        when(this.validator.validate(any())).thenReturn(Result.renewFailure(expectedResult));

        // Act
        var result = this.aesEncryptionService.encrypt(VALID_DATA, VALID_CONFIG);

        // Assert
        assertTrue(result.isFailure());
        assertEquals(Result.renewFailure(expectedResult), result);
    }

    @Test
    public void decrypt_whenTheConfigValidationReturnsFailed(){
        // Arrange
        var expectedResult = Result.failure(ErrorType.VALIDATION_ERROR, new Field("expected-instance", "expected-description"));
        when(this.validator.validate(any())).thenReturn(Result.renewFailure(expectedResult));

        // Act
        var result = this.aesEncryptionService.decrypt(VALID_DATA, VALID_CONFIG);

        // Assert
        assertTrue(result.isFailure());
        assertEquals(Result.renewFailure(expectedResult), result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  ", "   ", "     "})
    public void encrypt_whenKeyIsBlank(String key){
        // Arrange
        VALID_CONFIG.setSecretKey(key);

        var expectedResult = ValidationChain.create()
                .validateInternalNotBlank(key, "key-base-64")
                .build();

        when(this.validator.validate(any())).thenReturn(Result.success(null));

        // Act
        var result = this.aesEncryptionService.encrypt(VALID_DATA, VALID_CONFIG);

        // Assert
        assertTrue(result.isFailure());
        assertEquals(Result.renewFailure(expectedResult), result);
    }

    @ParameterizedTest
    @MethodSource("com.jobby.authorization.TestStreams#blankStringList")
    public void decrypt_whenKeyIsBlank(String key){
        // Arrange
        VALID_CONFIG.setSecretKey(key);

        var expectedResult = ValidationChain.create()
                .validateInternalNotBlank(key, "key-base-64")
                .build();

        when(this.validator.validate(any())).thenReturn(Result.success(null));

        // Act
        var result = this.aesEncryptionService.decrypt(VALID_CIPHER, VALID_CONFIG);

        // Assert
        assertTrue(result.isFailure());
        assertEquals(Result.renewFailure(expectedResult), result);
    }

    @Test
    public void encrypt_whenKeyIsNull(){
        // Arrange
        VALID_CONFIG.setSecretKey(null);

        var expectedResult = ValidationChain.create()
                .validateInternalNotBlank(null, "key-base-64")
                .build();
        when(this.validator.validate(any())).thenReturn(Result.success(null));

        // Act
        var result = this.aesEncryptionService.encrypt(VALID_DATA, VALID_CONFIG);

        // Assert
        assertTrue(result.isFailure());
        assertEquals(Result.renewFailure(expectedResult), result);
    }

    @Test
    public void decrypt_whenKeyIsNull(){
        // Arrange
        VALID_CONFIG.setSecretKey(null);

        var expectedResult = ValidationChain.create()
                .validateInternalNotBlank(null, "key-base-64")
                .build();

        when(this.validator.validate(any())).thenReturn(Result.success(null));

        // Act
        var result = this.aesEncryptionService.decrypt(VALID_CIPHER, VALID_CONFIG);

        // Assert
        assertTrue(result.isFailure());
        assertEquals(Result.renewFailure(expectedResult), result);
    }

    @Test
    public void encrypt_whenKeyIsInvalidInBase64(){
        // Arrange
        final String INVALID_KEY = "p652zw20jx/Bvg/4I7Mrdg==%";
        VALID_CONFIG.setSecretKey(INVALID_KEY);

        var expectedResult = Result.failure(ErrorType.ITS_SERIALIZATION_ERROR,
                new Field("key-base-64", "is invalid in base64"));
        when(this.validator.validate(any())).thenReturn(Result.success(null));

        // Act
        var result = this.aesEncryptionService.encrypt(VALID_DATA, VALID_CONFIG);

        // Assert
        assertFailure(result, Result.renewFailure(expectedResult));
    }

    @Test
    public void decrypt_whenKeyIsInvalidInBase64(){
        // Arrange
        final String INVALID_KEY = "p652zw20jx/Bvg/4I7Mrdg==%";
        VALID_CONFIG.setSecretKey(INVALID_KEY);

        var expectedResult = Result.failure(ErrorType.ITS_SERIALIZATION_ERROR,
                new Field("key-base-64", "is invalid in base64"));
        when(this.validator.validate(any())).thenReturn(Result.success(null));

        // Act
        var result = this.aesEncryptionService.decrypt(VALID_CIPHER, VALID_CONFIG);

        // Assert
        assertFailure(result, Result.renewFailure(expectedResult));
    }

    public String generateRandomKey(int length){
        var randomBytes = new byte[length];
        new Random().nextBytes(randomBytes);
        return Base64.getEncoder().encodeToString(randomBytes);
    }

    @ParameterizedTest
    @ValueSource(ints = {119, 1, 2, 20, 175, 258})
    public void encrypt_whenKeyLengthIsInvalid(int keyLength){
        // Arrange
        VALID_CONFIG.setSecretKey(generateRandomKey(keyLength));

        var expectedResult = ValidationChain.create()
                .validateInternalAnyMatch(keyLength, new Integer[]{128, 192, 256}, "key-base-64-bytes")
                .build();

        when(this.validator.validate(any())).thenReturn(Result.success(null));

        // Act
        var result = this.aesEncryptionService.encrypt(VALID_DATA, VALID_CONFIG);

        // Assert
        assertFailure(result, Result.renewFailure(expectedResult));
    }

    @ParameterizedTest
    @ValueSource(ints = {119, 1, 2, 20, 175, 258})
    public void decrypt_whenKeyLengthIsInvalid(int keyLength){
        // Arrange
        VALID_CONFIG.setSecretKey(generateRandomKey(keyLength));

        var expectedResult = ValidationChain.create()
                .validateInternalAnyMatch(keyLength, new Integer[]{128, 192, 256}, "key-base-64-bytes")
                .build();

        when(this.validator.validate(any())).thenReturn(Result.success(null));

        // Act
        var result = this.aesEncryptionService.decrypt(VALID_CIPHER, VALID_CONFIG);

        // Assert
        assertFailure(result, Result.renewFailure(expectedResult));
    }

    @Test
    public void encrypt_whenBuildReturnsFailure() {
        // Arrange
        var config = new EncryptConfig(
                VALID_KEY,
                new Iv(VALID_IV_LENGTH, VALID_T_LENGTH)
        );

        Result<byte[], Error> expectedResult = Result.failure(ErrorType.VALIDATION_ERROR, new Field("expectedInstance", "expectedReason"));

        when(this.validator.validate(any())).thenReturn(Result.success(null));

        when(this.defaultEncryptBuilder.setData(any())).thenReturn(defaultEncryptBuilder);
        when(this.defaultEncryptBuilder.setIv(any())).thenReturn(defaultEncryptBuilder);
        when(this.defaultEncryptBuilder.setKey(any())).thenReturn(defaultEncryptBuilder);
        when(this.defaultEncryptBuilder.setMode(Cipher.ENCRYPT_MODE)).thenReturn(defaultEncryptBuilder);
        when(this.defaultEncryptBuilder.setTransformation(any())).thenReturn(defaultEncryptBuilder);
        when(this.defaultEncryptBuilder.build()).thenReturn(expectedResult);

        // Act
        var result = this.aesEncryptionService.encrypt(VALID_DATA, config);

        // Assert
        assertTrue(result.isFailure());
        assertEquals(Result.renewFailure(expectedResult), result);
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2,3,50,97, 111, 119, 121,129})
    public void encrypt_whenTLenAreInvalid(int tLen){
        // Arrange
        VALID_CONFIG.getIv().setTLen(tLen);

        when(this.validator.validate(any())).thenReturn(Result.success(null));

        var expectedResult = ValidationChain.create().validateInternalAnyMatch(
                tLen * 8,
                new Integer[]{98, 112, 120, 128},
                "t-len").build();
        // Act
        var result = this.aesEncryptionService.encrypt(VALID_DATA, VALID_CONFIG);

        // Assert
        assertFailure(result, Result.renewFailure(expectedResult));
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2,3,50,97, 111, 119, 121,129})
    public void decrypt_whenTLenAreInvalid(int tLen){
        // Arrange
        VALID_CONFIG.getIv().setTLen(tLen);

        when(this.validator.validate(any())).thenReturn(Result.success(null));

        var expectedResult = ValidationChain.create().validateInternalAnyMatch(
                        tLen * 8,
                        new Integer[]{98, 112, 120, 128},
                        "t-len")
                .build();

        // Act
        var result = this.aesEncryptionService.decrypt(VALID_DATA, VALID_CONFIG);

        // Assert
        assertFailure(result, Result.renewFailure(expectedResult));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  ", "   ", "     "})
    public void decrypt_whenCipherTextAreBlank(String cipherText){
        // Arrange
        when(this.validator.validate(any())).thenReturn(Result.success(null));

        var expectedResult = ValidationChain.create()
                .validateInternalNotBlank(cipherText, "cipher-text")
                .build();

        // Act
        var result = this.aesEncryptionService.decrypt(cipherText, VALID_CONFIG);

        // Assert
        assertTrue(result.isFailure());
        assertEquals(Result.renewFailure(expectedResult), result);
    }

    @Test
    public void decrypt_whenCipherTextAreNull(){
        // Arrange
        when(this.validator.validate(any())).thenReturn(Result.success(null));

        var expectedResult = ValidationChain.create()
                .validateInternalNotBlank(null, "cipher-text")
                .build();
        // Act
        var result = this.aesEncryptionService.decrypt(null, VALID_CONFIG);

        // Assert
        assertTrue(result.isFailure());
        assertEquals(Result.renewFailure(expectedResult), result);
    }

    private void setUpBuilder(int mode){
        when(this.defaultEncryptBuilder.setData(any())).thenReturn(defaultEncryptBuilder);
        when(this.defaultEncryptBuilder.setIv(any())).thenReturn(defaultEncryptBuilder);
        when(this.defaultEncryptBuilder.setKey(any())).thenReturn(defaultEncryptBuilder);
        when(this.defaultEncryptBuilder.setMode(mode)).thenReturn(defaultEncryptBuilder);
        when(this.defaultEncryptBuilder.setTransformation(any())).thenReturn(defaultEncryptBuilder);
    }

    @Test
    public void encrypt_whenBuilderReturnsFailure() {
        // Arrange
        when(this.validator.validate(any())).thenReturn(Result.success(null));
        setUpBuilder(Cipher.ENCRYPT_MODE);
        var expectedResult = Result.failure(ErrorType.ITS_INVALID_OPTION_PARAMETER, new Field("expected-instance", "expected-reason"));
        when(this.defaultEncryptBuilder.build()).thenReturn(Result.renewFailure(expectedResult));

        // Act
        var result = this.aesEncryptionService.encrypt(VALID_DATA, VALID_CONFIG);

        // Assert
        assertTrue(result.isFailure());
        assertEquals(Result.renewFailure(expectedResult), result);
    }

    @Test
    public void encrypt_whenAllIsCorrect() {
        // Arrange
        when(this.validator.validate(any())).thenReturn(Result.success(null));
        setUpBuilder(Cipher.ENCRYPT_MODE);

        var resp = new byte[2];
        Result<byte[], Error> expectedResult = Result.success(resp);
        when(this.defaultEncryptBuilder.build()).thenReturn(expectedResult);

        // Act
        var result = this.aesEncryptionService.encrypt(VALID_DATA, VALID_CONFIG);

        // Assert
        assertTrue(result.isSuccess());
    }

    @Test
    public void decrypt_whenAllIsCorrect() {
        // Arrange
        when(this.validator.validate(any())).thenReturn(Result.success(null));
        setUpBuilder(Cipher.DECRYPT_MODE);

        var resp = new byte[2];
        Result<byte[], Error> expectedResult = Result.success(resp);
        when(this.defaultEncryptBuilder.build()).thenReturn(expectedResult);

        // Act
        var result = this.aesEncryptionService.decrypt(VALID_CIPHER, VALID_CONFIG);

        // Assert
        assertTrue(result.isSuccess());
    }

    @Test
    public void decrypt_whenCombinedIsInvalid() {
        // Arrange
        when(this.validator.validate(any())).thenReturn(Result.success(null));
        var expectedResult = ValidationChain.create()
                .validateInternalGreaterThan(0 ,12, "combined")
                .build();

        // Act
        var result = this.aesEncryptionService.decrypt(VALID_CIPHER.substring(0, VALID_IV_LENGTH - 1), VALID_CONFIG);

        // Assert
        assertFailure(result, Result.renewFailure(expectedResult));
    }

    @Test
    public void decrypt_whenCipherTextAreInvalidInBase64() {
        // Arrange
        final String INVALID_CIPHER_TEXT = "invalid-cipher%%";

        when(this.validator.validate(any())).thenReturn(Result.success(null));

        var expectedResult = Result.failure(ErrorType.ITS_SERIALIZATION_ERROR,
                new Field(
                        "cipherText",
                        "Invalid Base64 encoded cipher text"
                )
        );

        // Act
        var result = this.aesEncryptionService.decrypt(INVALID_CIPHER_TEXT, VALID_CONFIG);

        // Assert
        assertFailure(result, Result.renewFailure(expectedResult));
    }

    @Test
    public void encrypt_decrypt_whenAllIsCorrect(){
        // Arrange
        when(this.validator.validate(any())).thenReturn(Result.success(null));
        setUpBuilder(Cipher.DECRYPT_MODE);
        setUpBuilder(Cipher.ENCRYPT_MODE);

        var resp = new byte[2];
        Result<byte[], Error> expectedResult = Result.success(resp);
        when(this.defaultEncryptBuilder.build()).thenReturn(expectedResult);

        // Act
        var result = this.aesEncryptionService.encrypt(VALID_DATA, VALID_CONFIG)
                .flatMap(combined -> this.aesEncryptionService.decrypt(combined, VALID_CONFIG));

        // Assert
        assertTrue(result.isSuccess());
    }
}
