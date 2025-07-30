package com.jobby.authorization.infraestructure.adapters.out.encrypt;

import com.jobby.authorization.domain.result.ErrorType;
import com.jobby.authorization.domain.result.Field;
import com.jobby.authorization.domain.result.Result;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

@ExtendWith(MockitoExtension.class)
public class AESEncryptionServiceTest {

    @Mock
    private DefaultEncryptBuilder defaultEncryptBuilder;

    @InjectMocks
    private AESEncryptionService aesEncryptionService;

    @ParameterizedTest
    @ValueSource(ints = { -2, -200, 0, 17, 200})
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
        Assertions.assertTrue(result.isFailure());
        Assertions.assertEquals(expectedResult, result);
    }


    @Test
    public void encrypt_whenKeyAreNull() {
        // Arrange
        var data = "example_data";
        var ivLength= 12;

        var expectedResult = Result.failure(ErrorType.ITN_INVALID_OPTION_PARAMETER,
                new Field(
                        "keyBase64",
                        "Encryption key cannot be empty"
                )
        );

        // Act
        var result = this.aesEncryptionService.encrypt(data, null, ivLength);

        // Assert
        Assertions.assertTrue(result.isFailure());
        Assertions.assertEquals(expectedResult, result);

    }


    @ParameterizedTest
    @ValueSource(strings = { "", " ", "     "})
    public void encrypt_whenKeyAreBlank(String key) {
        // Arrange
        var data = "example_data";
        var ivLength= 12;

        var expectedResult = Result.failure(ErrorType.ITN_INVALID_OPTION_PARAMETER,
                new Field(
                        "keyBase64",
                        "Encryption key cannot be empty"
                )
        );

        // Act
        var result = this.aesEncryptionService.encrypt(data, key, ivLength);

        // Assert
        Assertions.assertTrue(result.isFailure());
        Assertions.assertEquals(expectedResult, result);

    }

    @Test
    public void encrypt_whenKeyAreInvalid(){
        // Arrange
        var data = "example_data";
        var ivLength= 12;
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
        Assertions.assertTrue(result.isFailure());
        Assertions.assertEquals(expectedResult, result);

    }

    @ParameterizedTest
    @ValueSource(ints = {  1, 2, 15, 33, 45 })
    public void encrypt_whenKeyLengthIsInvalid(int keyLength) throws NoSuchAlgorithmException {
        // Arrange
        var data = "example_data";
        var ivLength= 12;
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
        Assertions.assertTrue(result.isFailure());
        Assertions.assertEquals(expectedResult, result);


    }

}
