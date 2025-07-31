package com.jobby.authorization.infraestructure.adapters.out.encrypt;

import com.jobby.authorization.domain.result.Error;
import com.jobby.authorization.domain.result.ErrorType;
import com.jobby.authorization.domain.result.Field;
import com.jobby.authorization.domain.result.Result;
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

    @Mock
    private DefaultEncryptBuilder defaultEncryptBuilder;

    @InjectMocks
    private AESEncryptionService aesEncryptionService;

    @ParameterizedTest
    @ValueSource(ints = {-2, -200, 0, 17, 200})
    public void encrypt_whenIvLengthAreInvalid(int ivLength) {
        // Arrange
        var data = "example_data";
        var key = "p652zw20jx/Bvg/4I7Mrdg==";

        var expectedResult = Result.failure(ErrorType.ITN_INVALID_OPTION_PARAMETER,
                new Field(
                        "ivLength",
                        "IV length must be between 1 and 16 bytes"
                )
        );
        // Act
        var result = this.aesEncryptionService.encrypt(data, key, ivLength);

        // Assert
        assertTrue(result.isFailure());
        assertEquals(expectedResult, result);
    }


    @Test
    public void encrypt_whenKeyAreNull() {
        // Arrange
        var data = "example_data";
        var ivLength = 12;

        var expectedResult = Result.failure(ErrorType.ITN_INVALID_OPTION_PARAMETER,
                new Field(
                        "keyBase64",
                        "Encryption key cannot be empty"
                )
        );

        // Act
        var result = this.aesEncryptionService.encrypt(data, null, ivLength);

        // Assert
        assertTrue(result.isFailure());
        assertEquals(expectedResult, result);

    }


    @ParameterizedTest
    @ValueSource(strings = {"", " ", "     "})
    public void encrypt_whenKeyAreBlank(String key) {
        // Arrange
        var data = "example_data";
        var ivLength = 12;

        var expectedResult = Result.failure(ErrorType.ITN_INVALID_OPTION_PARAMETER,
                new Field(
                        "keyBase64",
                        "Encryption key cannot be empty"
                )
        );

        // Act
        var result = this.aesEncryptionService.encrypt(data, key, ivLength);

        // Assert
        assertTrue(result.isFailure());
        assertEquals(expectedResult, result);

    }

    @Test
    public void encrypt_whenKeyAreInvalid() {
        // Arrange
        var data = "example_data";
        var ivLength = 12;
        var key = "invalid_base64_key";

        var expectedResult = Result.failure(ErrorType.ITN_INVALID_OPTION_PARAMETER,
                new Field(
                        "keyBase64",
                        "Invalid Base64 encoded key or incompatible key format"
                )
        );

        // Act
        var result = this.aesEncryptionService.encrypt(data, key, ivLength);

        // Assert
        assertTrue(result.isFailure());
        assertEquals(expectedResult, result);

    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 15, 33, 45})
    public void encrypt_whenKeyLengthIsInvalid(int keyLength) {
        // Arrange
        var data = "example_data";
        var ivLength = 12;
        var keyBytes = new byte[keyLength];
        new Random().nextBytes(keyBytes);
        var keyBase64 = Base64.getEncoder().encodeToString(keyBytes);

        var expectedResult = Result.failure(ErrorType.ITN_INVALID_OPTION_PARAMETER,
                new Field(
                        "keyBase64",
                        "Key length must be between 16 and 32 bytes for AES encryption"
                )
        );

        // Act
        var result = this.aesEncryptionService.encrypt(data, keyBase64, ivLength);

        // Assert
        assertTrue(result.isFailure());
        assertEquals(expectedResult, result);
    }

    @Test
    public void encrypt_whenBuildReturnsFailure() {
        // Arrange
        var data = "example_data";
        var ivLength = 12;

        Result<byte[], Error> expectedResult = Result.failure(ErrorType.VALIDATION_ERROR, new Field("expectedInstance", "expectedReason"));

        when(this.defaultEncryptBuilder.setData(any())).thenReturn(defaultEncryptBuilder);
        when(this.defaultEncryptBuilder.setIv(any())).thenReturn(defaultEncryptBuilder);
        when(this.defaultEncryptBuilder.setKey(any())).thenReturn(defaultEncryptBuilder);
        when(this.defaultEncryptBuilder.setMode(Cipher.ENCRYPT_MODE)).thenReturn(defaultEncryptBuilder);
        when(this.defaultEncryptBuilder.setTransformation(any())).thenReturn(defaultEncryptBuilder);
        when(this.defaultEncryptBuilder.build()).thenReturn(expectedResult);

        // Act
        var result = this.aesEncryptionService.encrypt(data, VALID_KEY, ivLength);

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
        var ivLength = 12;

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
        var result = this.aesEncryptionService.encrypt(data, VALID_KEY, ivLength);

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
        var ivLength = 12;
        var expectedResult = Result.failure(ErrorType.INVALID_INPUT,
                new Field(
                        "cipherText",
                        "Cipher text cannot be null or blank"
                )
        );

        // Act
        var result = this.aesEncryptionService.decrypt(null, VALID_KEY, ivLength);

        // Assert
        assertTrue(result.isFailure());
        assertEquals(expectedResult, result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  ", "         "})
    public void decrypt_whenCipherTextAreBlank(String cipherText) {
        // Arrange
        var ivLength = 12;
        var expectedResult = Result.failure(ErrorType.INVALID_INPUT,
                new Field(
                        "cipherText",
                        "Cipher text cannot be null or blank"
                )
        );

        // Act
        var result = this.aesEncryptionService.decrypt(cipherText, VALID_KEY, ivLength);

        // Assert
        assertTrue(result.isFailure());
        assertEquals(expectedResult, result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"example1-invalid", "cfowfm233", "words-connected", "example2example3###"})
    public void decrypt_whenCipherTextAreBase64Invalid(String cipherText) {
        // Arrange
        var ivLength = 12;
        var expectedResult = Result.failure(ErrorType.INVALID_INPUT,
                new Field(
                        "cipherText",
                        "Invalid Base64 encoded cipher text"
                )
        );

        // Act
        var result = this.aesEncryptionService.decrypt(cipherText, VALID_KEY, ivLength);

        // Assert
        assertTrue(result.isFailure());
        assertEquals(expectedResult, result);
    }

    @RepeatedTest(100)
    public void decrypt_whenCipherTextLengthAreInvalid(){
        // Arrange
        int ivLength = 12;
        var cipherBytes = new byte[ivLength-1]; // It has to be bigger than the iv length
        new Random().nextBytes(cipherBytes);
        var cipherText = Base64.getEncoder().encodeToString(cipherBytes);

        var expectedResult = Result.failure(ErrorType.VALIDATION_ERROR,
                new Field(
                        "cipherText",
                        "Cipher text is too short to contain valid data (expected at least " + ivLength + " bytes)"
                )
        );

        // Act
        var result = this.aesEncryptionService.decrypt(cipherText, VALID_KEY, ivLength);

        // Assert
        assertTrue(result.isFailure());
        assertEquals(expectedResult, result);
    }

    @ParameterizedTest
    @ValueSource(ints = {12,34,100})
    public void decrypt_WhenEncryptBuilderReturnsFailed(int cipherTextLength) {
        // Arrange
        int ivLength = 12;
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
        var result = this.aesEncryptionService.decrypt(cipherText, VALID_KEY, ivLength);

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
        var ivLength = 12;
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
        var result = this.aesEncryptionService.decrypt(cipherText, VALID_KEY, ivLength);

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
