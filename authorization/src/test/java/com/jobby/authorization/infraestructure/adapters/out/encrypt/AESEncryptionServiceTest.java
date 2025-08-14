package com.jobby.authorization.infraestructure.adapters.out.encrypt;

import com.jobby.authorization.domain.ports.out.SafeResultValidator;
import com.jobby.authorization.domain.shared.result.Error;
import com.jobby.authorization.domain.shared.result.ErrorType;
import com.jobby.authorization.domain.shared.result.Field;
import com.jobby.authorization.domain.shared.result.Result;
import com.jobby.authorization.infraestructure.config.EncryptConfig;
import com.jobby.authorization.infraestructure.config.EncryptConfig.Iv;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import javax.crypto.Cipher;
import java.util.Base64;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AESEncryptionServiceTest {

    private final static String VALID_KEY = "p652zw20jx/Bvg/4I7Mrdg==";
    private final static int VALID_IV_LENGTH = 12;
    private final static int VALID_T_LENGTH = 128;
    private final static String VALID_DATA = "Hello World";
    private final static EncryptConfig VALID_CONFIG = new EncryptConfig(VALID_KEY, new Iv(VALID_IV_LENGTH, VALID_T_LENGTH));

    @Mock
    private SafeResultValidator validator;

    @Mock
    private DefaultEncryptBuilder defaultEncryptBuilder;

    @InjectMocks
    private AESEncryptionService aesEncryptionService;

    @Test
    public void encrypt_whenTheConfigValidationReturnsFailed(){
        // Arrange
        var expectedResult = Result.failure(ErrorType.VALIDATION_ERROR, new Field("expected-instance", "expected-description"));
        when(this.validator.validate(any())).thenReturn(Result.renewFailure(expectedResult));

        // Act
        var result = this.aesEncryptionService.decrypt(VALID_DATA, VALID_CONFIG);

        // Assert
        assertTrue(result.isFailure());
        assertEquals(Result.renewFailure(expectedResult), result);
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
    @ValueSource(strings = {
            "secret-name",
            "compose.key.username.password.pattern",
            "email@example.com",
            "secret-info",
            "most-info",
            "secret-name2"})
    public void encrypt_whenAllIsCorrect(String data) {
        // Arrange
        var config = new EncryptConfig(
                VALID_KEY,
                new Iv(VALID_IV_LENGTH, VALID_T_LENGTH)
        );

        var expectedResultResponse = new byte[16];
        new Random().nextBytes(expectedResultResponse);
        Result<byte[], Error> expectedResult = Result.success(expectedResultResponse);

        when(this.defaultEncryptBuilder.setData(any())).thenReturn(defaultEncryptBuilder);
        when(this.defaultEncryptBuilder.setIv(any())).thenReturn(defaultEncryptBuilder);
        when(this.defaultEncryptBuilder.setKey(any())).thenReturn(defaultEncryptBuilder);
        when(this.defaultEncryptBuilder.setMode(Cipher.ENCRYPT_MODE)).thenReturn(defaultEncryptBuilder);
        when(this.defaultEncryptBuilder.setTransformation(any())).thenReturn(defaultEncryptBuilder);
        when(this.defaultEncryptBuilder.build()).thenReturn(expectedResult);

        // Act
        var result = this.aesEncryptionService.encrypt(data, config);

        // Assert
        assertTrue(result.isSuccess());
        verify(defaultEncryptBuilder, times(1)).setData(any());
        verify(defaultEncryptBuilder, times(1)).setMode(Cipher.ENCRYPT_MODE);
        verify(defaultEncryptBuilder, times(1)).setIv(any());
        verify(defaultEncryptBuilder, times(1)).setKey(any());
        verify(defaultEncryptBuilder, times(1)).setTransformation(any());
        verify(defaultEncryptBuilder, times(1)).build();
    }

    @Test
    public void decrypt_WhenCipherTextAreNull() {
        // Arrange
        var config = new EncryptConfig(
                VALID_KEY,
                new Iv(VALID_IV_LENGTH, VALID_T_LENGTH)
        );

        var expectedResult = Result.failure(ErrorType.INVALID_INPUT,
                new Field(
                        "cipherText",
                        "Cipher text cannot be null or blank"
                )
        );

        // Act
        var result = this.aesEncryptionService.decrypt(null, config);

        // Assert
        assertTrue(result.isFailure());
        assertEquals(expectedResult, result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  ", "         "})
    public void decrypt_whenCipherTextAreBlank(String cipherText) {
        // Arrange
        var config = new EncryptConfig(
                VALID_KEY,
                new Iv(VALID_IV_LENGTH, VALID_T_LENGTH)
        );

        var expectedResult = Result.failure(ErrorType.INVALID_INPUT,
                new Field(
                        "cipherText",
                        "Cipher text cannot be null or blank"
                )
        );

        // Act
        var result = this.aesEncryptionService.decrypt(cipherText, config);

        // Assert
        assertTrue(result.isFailure());
        assertEquals(expectedResult, result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"example1-invalid", "cfowfm233", "words-connected", "example2example3###"})
    public void decrypt_whenCipherTextAreBase64Invalid(String cipherText) {
        // Arrange
        var config = new EncryptConfig(
                VALID_KEY,
                new Iv(VALID_IV_LENGTH, VALID_T_LENGTH)
        );


        var expectedResult = Result.failure(ErrorType.INVALID_INPUT,
                new Field(
                        "cipherText",
                        "Invalid Base64 encoded cipher text"
                )
        );

        // Act
        var result = this.aesEncryptionService.decrypt(cipherText, config);

        // Assert
        assertTrue(result.isFailure());
        assertEquals(expectedResult, result);
    }

    @RepeatedTest(100)
    public void decrypt_whenCipherTextLengthAreInvalid(){
        // Arrange
        var config = new EncryptConfig(
                VALID_KEY,
                new Iv(VALID_IV_LENGTH, VALID_T_LENGTH)
        );

        var cipherBytes = new byte[VALID_IV_LENGTH-1]; // It has to be bigger than the iv length
        new Random().nextBytes(cipherBytes);
        var cipherText = Base64.getEncoder().encodeToString(cipherBytes);

        var expectedResult = Result.failure(ErrorType.VALIDATION_ERROR,
                new Field(
                        "cipherText",
                        "Cipher text is too short to contain valid data (expected at least " + VALID_IV_LENGTH + " bytes)"
                )
        );

        // Act
        var result = this.aesEncryptionService.decrypt(cipherText, config);

        // Assert
        assertTrue(result.isFailure());
        assertEquals(expectedResult, result);
    }

    @ParameterizedTest
    @ValueSource(ints = {12,34,100})
    public void decrypt_WhenEncryptBuilderReturnsFailed(int cipherTextLength) {
        // Arrange
        var config = new EncryptConfig(
                VALID_KEY,
                new Iv(VALID_IV_LENGTH, VALID_T_LENGTH)
        );

        var cipherBytes = new byte[cipherTextLength];
        new Random().nextBytes(cipherBytes);
        var cipherText = Base64.getEncoder().encodeToString(cipherBytes);

        Result<byte[], Error> expectedResult = Result.failure(ErrorType.VALIDATION_ERROR,
                new Field(
                        "expectedInstance",
                        "expectedReason"
                )
        );

        when(this.defaultEncryptBuilder.setData(any())).thenReturn(defaultEncryptBuilder);
        when(this.defaultEncryptBuilder.setIv(any())).thenReturn(defaultEncryptBuilder);
        when(this.defaultEncryptBuilder.setKey(any())).thenReturn(defaultEncryptBuilder);
        when(this.defaultEncryptBuilder.setMode(Cipher.DECRYPT_MODE)).thenReturn(defaultEncryptBuilder);
        when(this.defaultEncryptBuilder.setTransformation(any())).thenReturn(defaultEncryptBuilder);
        when(this.defaultEncryptBuilder.build()).thenReturn(expectedResult);

        // Act
        var result = this.aesEncryptionService.decrypt(cipherText, config);

        // Assert
        assertTrue(result.isFailure());
        assertEquals(Result.renewFailure(expectedResult), result);
        verify(defaultEncryptBuilder, times(1)).setData(any());
        verify(defaultEncryptBuilder, times(1)).setMode(Cipher.DECRYPT_MODE);
        verify(defaultEncryptBuilder, times(1)).setIv(any());
        verify(defaultEncryptBuilder, times(1)).setKey(any());
        verify(defaultEncryptBuilder, times(1)).setTransformation(any());
        verify(defaultEncryptBuilder, times(1)).build();
    }


    @RepeatedTest(1)
    public void decrypt_whenAllIsCorrect() {
        // Arrange
        var config = new EncryptConfig(
                VALID_KEY,
                new Iv(VALID_IV_LENGTH, VALID_T_LENGTH)
        );
        var cipherBytes = new byte[15];
        new Random().nextBytes(cipherBytes);
        var cipherText = Base64.getEncoder().encodeToString(cipherBytes);

        Result<byte[], Error> encryptBuildResult = Result.success(cipherBytes);

        when(this.defaultEncryptBuilder.setData(any())).thenReturn(defaultEncryptBuilder);
        when(this.defaultEncryptBuilder.setIv(any())).thenReturn(defaultEncryptBuilder);
        when(this.defaultEncryptBuilder.setKey(any())).thenReturn(defaultEncryptBuilder);
        when(this.defaultEncryptBuilder.setMode(Cipher.DECRYPT_MODE)).thenReturn(defaultEncryptBuilder);
        when(this.defaultEncryptBuilder.setTransformation(any())).thenReturn(defaultEncryptBuilder);
        when(this.defaultEncryptBuilder.build()).thenReturn(encryptBuildResult);
        // Act
        var result = this.aesEncryptionService.decrypt(cipherText, config);

        // Assert
        assertTrue(result.isSuccess());
        verify(defaultEncryptBuilder, times(1)).setData(any());
        verify(defaultEncryptBuilder, times(1)).setMode(Cipher.DECRYPT_MODE);
        verify(defaultEncryptBuilder, times(1)).setIv(any());
        verify(defaultEncryptBuilder, times(1)).setKey(any());
        verify(defaultEncryptBuilder, times(1)).setTransformation(any());
        verify(defaultEncryptBuilder, times(1)).build();
    }

}
