package com.jobby.authorization.infraestructure.adapters.out.encrypt;

import com.jobby.authorization.domain.result.ErrorType;
import com.jobby.authorization.domain.result.Field;
import com.jobby.authorization.domain.result.Result;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

public class DecryptEncryptBuilderTest {

    private DefaultEncryptBuilder defaultEncryptBuilder = new DefaultEncryptBuilder();

    @Test
    public void build_whenInstanceIsNull() {
        // Arrange
        var resp = Result.failure(ErrorType.ITN_INVALID_OPTION_PARAMETER,
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
        Assertions.assertFalse(build.isSuccess());
        Assertions.assertEquals(build, resp);
    }

    @ParameterizedTest
    @ValueSource(strings = {"SHA-256", "NOTHING", "SHA"})
    public void build_whenInstanceIsInvalid(String input) {
        // Arrange
        var resp = Result.failure(ErrorType.ITN_INVALID_OPTION_PARAMETER,
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
        Assertions.assertFalse(build.isSuccess());
        Assertions.assertEquals(build, resp);
    }


    @Test
    public void build_whenKeyOrIvIsNull_Encrypt() throws NoSuchAlgorithmException {
        // Arrange
        var resp = Result.failure(ErrorType.ITN_OPERATION_ERROR,
                new Field[]{
                        new Field(
                                "this.key",
                                "Possible invalid Key mode: " + null
                        ),
                        new Field(
                                "this.mode",
                                "Possible invalid cipher mode: " + 0
                        ),
                        new Field(
                                "this.Iv",
                                "Possible invalid cipher iv: " + 0
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
        Assertions.assertFalse(buildWithOutIv.isSuccess());
        Assertions.assertFalse(buildWithOutKey.isSuccess());
        Assertions.assertEquals(buildWithOutIv, buildWithOutKey);
        Assertions.assertEquals(buildWithOutIv, resp);
    }

    @RepeatedTest(5)
    public void build_whenKeyIsInvalid_Encrypt() throws NoSuchAlgorithmException {
        // Arrange
        var key = EncryptUtils.generateKey("AES", 192);
        var iv = EncryptUtils.generateIv(12, 128);
        var resp = Result.failure(ErrorType.ITN_OPERATION_ERROR,
                new Field[]{
                        new Field(
                                "this.key",
                                "Possible invalid Key: " + key.toString()
                        ),
                        new Field(
                                "this.mode",
                                "Possible invalid cipher mode: " + Cipher.ENCRYPT_MODE
                        ),
                        new Field(
                                "this.Iv",
                                "Possible invalid cipher iv: " + iv.toString()
                        )
                }
        );

        // Act
        var build = this.defaultEncryptBuilder
                .setTransformation("AES/GCM/NoPadding")
                .setKey(key)
                .setIv(iv)
                .setMode(Cipher.ENCRYPT_MODE)
                .setData("Compose".getBytes(StandardCharsets.UTF_8))
                .build();

        // Asserts
        Assertions.assertFalse(build.isSuccess());
        Assertions.assertEquals(build, resp);

    }

}
