package com.jobby.authorization.infraestructure.adapters.out.encrypt;

import com.jobby.domain.mobility.ErrorType;
import com.jobby.authorization.domain.shared.errors.Field;
import com.jobby.authorization.domain.shared.result.Result;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultEncryptBuilderTest {

    private final DefaultEncryptBuilder defaultEncryptBuilder = new DefaultEncryptBuilder();

    @Test
    public void build_whenInstanceIsNull() {
        // Arrange
        var resp = Result.failure(ErrorType.ITS_INVALID_OPTION_PARAMETER,
                new Field(
                        "transformation",
                        "Invalid cipher transformation: " + null
                )
        );

        // Act
        var build = this.defaultEncryptBuilder
                .setTransformation(null)
                .build();

        // Assert
        assertFalse(build.isSuccess());
        assertEquals(build, resp);
    }

    @ParameterizedTest
    @ValueSource(strings = {"SHA-256", "NOTHING", "SHA"})
    public void build_whenInstanceIsInvalid(String input) {
        // Arrange
        var resp = Result.failure(ErrorType.ITS_INVALID_OPTION_PARAMETER,
                new Field(
                        "transformation",
                        "Invalid cipher transformation: " + input
                )
        );

        // Act
        var build = this.defaultEncryptBuilder
                .setTransformation(input)
                .build();

        // Assert
        assertFalse(build.isSuccess());
        assertEquals(build, resp);
    }


    @Test
    public void build_whenKeyOrIvIsNull_Encrypt() {
        // Arrange
        var resp = Result.failure(ErrorType.ITS_OPERATION_ERROR,
                new Field[]{
                        new Field(
                                "this.key",
                                "Possible invalid Key: " + null
                        ),
                        new Field(
                                "this.mode",
                                "Possible invalid cipher mode: " + 0
                        ),
                        new Field(
                                "this.Iv",
                                "Possible invalid cipher iv: " + null
                        )
                }
        );
        // Act
        var buildWithOutIv = this.defaultEncryptBuilder
                .setTransformation("AES/GCM/NoPadding")
                .setIv(null)
                .build();

        var buildWithOutKey = this.defaultEncryptBuilder
                .setTransformation("AES/GCM/NoPadding")
                .setKey(null)
                .build();

        // Assert
        assertFalse(buildWithOutIv.isSuccess());
        assertFalse(buildWithOutKey.isSuccess());
        assertEquals(buildWithOutIv, buildWithOutKey);
        assertEquals(buildWithOutIv, resp);
    }

    @RepeatedTest(100)
    public void build_whenKeyIsInvalid_Encrypt() {
        // Arrange
        var key = new SecretKeySpec(new byte[5], "AES"); // 5 bytes, invalid key
        var iv = EncryptUtils.generateIv(12, 128);
        var resp = Result.failure(ErrorType.ITS_OPERATION_ERROR,
                new Field[]{
                        new Field(
                                "this.key",
                                "Possible invalid Key: " + key
                        ),
                        new Field(
                                "this.mode",
                                "Possible invalid cipher mode: " + 100
                        ),
                        new Field(
                                "this.Iv",
                                "Possible invalid cipher iv: " + iv
                        )
                }
        );

        // Act
        var build = this.defaultEncryptBuilder
                .setTransformation("AES/GCM/NoPadding")
                .setKey(key)
                .setIv(iv)
                .setMode(100)
                .setData("Compose".getBytes(StandardCharsets.UTF_8))
                .build();

        // Asserts
        assertFalse(build.isSuccess());
        assertEquals(build, resp);

    }

    @RepeatedTest(100)
    public void build_whenKeyIvInvalid_Encrypt() throws NoSuchAlgorithmException {
        // Arrange
        var key = EncryptUtils.generateKey("AES", 256);
        var iv = new GCMParameterSpec(128, new byte[0]); // invalid iv with empty length
        var resp = Result.failure(ErrorType.ITS_OPERATION_ERROR,
                new Field[]{
                        new Field(
                                "this.key",
                                "Possible invalid Key: " + key
                        ),
                        new Field(
                                "this.mode",
                                "Possible invalid cipher mode: " + 100
                        ),
                        new Field(
                                "this.Iv",
                                "Possible invalid cipher iv: " + iv
                        )
                }
        );

        // Act
        var build = this.defaultEncryptBuilder
                .setTransformation("AES/GCM/NoPadding")
                .setKey(key)
                .setIv(iv)
                .setMode(100)
                .setData("Compose".getBytes(StandardCharsets.UTF_8))
                .build();

        // Asserts
        assertFalse(build.isSuccess());
        assertEquals(build, resp);

    }

    @RepeatedTest(10)
    public void build_whenTransitionBlockSizeAreInvalid() throws NoSuchAlgorithmException {
        // Arrange
        var key = EncryptUtils.generateKey("AES", 128);
        var data = new byte[10];
        new Random().nextBytes(data);

        var resp = Result.failure(ErrorType.ITS_OPERATION_ERROR,
                new Field(
                        "this.data",
                        "Cipher operation failed - data may be corrupted or incompatible"
                )
        );

        // Act
        var build = this.defaultEncryptBuilder
                .setTransformation("AES/ECB/NoPadding")
                // In this mode the data has to be multiple of the key length, if not it returns error
                .setKey(key)
                .setIv(null)
                // This mode not use iv
                .setMode(Cipher.ENCRYPT_MODE)
                .setData(data)
                .build();

        // Asserts
        assertFalse(build.isSuccess());
        assertEquals(build, resp);
    }

    @RepeatedTest(100)
    public void build_whenTransitionPaddingAreBad() throws NoSuchAlgorithmException {
        // Arrange
        var key = EncryptUtils.generateKey("AES", 128);
        var iv = new IvParameterSpec(new byte[16]);
        var data = new byte[33];
        new Random().nextBytes(data);

        var resp = Result.failure(ErrorType.ITS_OPERATION_ERROR,
                new Field(
                        "this.data",
                        "Cipher operation failed - data may be corrupted or incompatible"
                )
        );

        // Act
        var build = this.defaultEncryptBuilder
                .setTransformation("AES/CBC/PKCS5Padding")
                // In this mode the data has to be multiple of the key length, if not it returns error
                .setKey(key)
                .setIv(iv)
                // This mode not use iv
                .setMode(Cipher.DECRYPT_MODE)
                .setData(data)
                .build();

        // Asserts
        assertFalse(build.isSuccess());
        assertEquals(build, resp);
    }

    @RepeatedTest(100)
    public void build_whenAllAreCorrect_Encrypt() throws NoSuchAlgorithmException {
        // Arrange
        var key = EncryptUtils.generateKey("AES", 256);
        var iv = EncryptUtils.generateIv(12, 128);
        var data = new byte[32];
        new Random().nextBytes(data);

        // Act
        var build = this.defaultEncryptBuilder
                .setTransformation("AES/GCM/NoPadding")
                .setKey(key)
                .setIv(iv)
                .setMode(Cipher.ENCRYPT_MODE)
                .setData(data)
                .build();

        // Asserts
        assertTrue(build.isSuccess());
    }

    @RepeatedTest(1)
    public void build_whenAllAreCorrect_Decrypt_Equals() throws NoSuchAlgorithmException {
        // Arrange
        var key = EncryptUtils.generateKey("AES", 256);
        var iv = EncryptUtils.generateIv(12, 128);
        var data = new byte[32];
        new Random().nextBytes(data);
        var initValue = Base64.getEncoder().encodeToString(data);
        String finalValue;
        // Act
        var build = this.defaultEncryptBuilder
                .setTransformation("AES/GCM/NoPadding")
                .setKey(key)
                .setIv(iv)
                .setMode(Cipher.ENCRYPT_MODE)
                .setData(data)
                .build()
                .flatMap((cipherBytes) ->
                    this.defaultEncryptBuilder
                            .setTransformation("AES/GCM/NoPadding")
                            .setKey(key)
                            .setIv(iv)
                            .setMode(Cipher.DECRYPT_MODE)
                            .setData(cipherBytes)
                            .build()
                );

        finalValue = Base64.getEncoder().encodeToString(build.getData());
        // Asserts
        assertTrue(build.isSuccess());
        assertEquals(finalValue, initValue);
    }

}
