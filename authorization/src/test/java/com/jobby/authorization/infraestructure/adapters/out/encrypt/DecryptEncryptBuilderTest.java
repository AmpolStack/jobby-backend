package com.jobby.authorization.infraestructure.adapters.out.encrypt;

import com.jobby.authorization.domain.result.ErrorType;
import com.jobby.authorization.domain.result.Field;
import com.jobby.authorization.domain.result.Result;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

public class DecryptEncryptBuilderTest {

    private final DefaultEncryptBuilder defaultEncryptBuilder = new DefaultEncryptBuilder();

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
    public void build_whenKeyOrIvIsNull_Encrypt() {
        // Arrange
        var resp = Result.failure(ErrorType.ITN_OPERATION_ERROR,
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
        Assertions.assertFalse(buildWithOutIv.isSuccess());
        Assertions.assertFalse(buildWithOutKey.isSuccess());
        Assertions.assertEquals(buildWithOutIv, buildWithOutKey);
        Assertions.assertEquals(buildWithOutIv, resp);
    }

    @RepeatedTest(100)
    public void build_whenKeyIsInvalid_Encrypt() {
        // Arrange
        var key = new SecretKeySpec(new byte[5], "AES"); // 5 bytes, invalid key
        var iv = EncryptUtils.generateIv(12, 128);
        var resp = Result.failure(ErrorType.ITN_OPERATION_ERROR,
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
        Assertions.assertFalse(build.isSuccess());
        Assertions.assertEquals(build, resp);

    }

    @RepeatedTest(100)
    public void build_whenKeyIvInvalid_Encrypt() throws NoSuchAlgorithmException {
        // Arrange
        var key = EncryptUtils.generateKey("AES", 256);
        var iv = new GCMParameterSpec(128, new byte[0]); // invalid iv with empty length
        var resp = Result.failure(ErrorType.ITN_OPERATION_ERROR,
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
        Assertions.assertFalse(build.isSuccess());
        Assertions.assertEquals(build, resp);

    }

}
