package com.jobby.authorization.infraestructure.out;

import com.jobby.authorization.infraestructure.adapters.out.AESEncryptionService;
import com.jobby.authorization.infraestructure.adapters.out.AESGenerators;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class AESEncryptionServiceTest {

    private final AESEncryptionService aesEncryptionService = new AESEncryptionService();

    private static SecretKey secretKey;
    private GCMParameterSpec iv;

    @BeforeAll
    public static void beforeAll() throws NoSuchAlgorithmException {
        secretKey = AESGenerators.generateKey();
    }

    @BeforeEach
    public void beforeEach() {
        this.iv = AESGenerators.generateIv();
    }


    @ParameterizedTest
    @ValueSource(strings = {
            "privateData",
            "privatedata@gmail.com",
            "+12 431-213-21",
            "CCV-340",
            "{ secure: {address: 12-2, phone: 214-33}, admin: false}"})
    public void encryptAndDecrypt_oneInput_WhenInputIsValid(String value) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        // Act
        var encryptText = this.aesEncryptionService.encrypt(value, secretKey, this.iv);
        var decryptText = this.aesEncryptionService.decrypt(encryptText, secretKey);

        // Assert
        Assertions.assertEquals(value, decryptText);
    }

    @Test
    public void encrypt_WhenInputIsNull_ThenExceptionIsThrown() {
        // Act & Assert
        Assertions.assertThrows(NullPointerException.class, () -> this.aesEncryptionService.encrypt(null, secretKey, iv));
    }

    @Test
    public void encrypt_WhenIVIsInvalidOrNull_ThenExceptionIsThrown() {
        // Act & Assert
        Assertions.assertThrows(NullPointerException.class, () -> this.aesEncryptionService.encrypt("thing", secretKey, null));
    }

    @Test
    public void encrypt_WhenSecretKeyIsOrInvalidNull_ThenExceptionIsThrown() {
        // Act & Assert
        var invalidKeyText = "invalidKeyText";
        // If the key byte array length is less than 128 it is invalid.
        // in this example the length equals 14
        var invalidKeyBytes = invalidKeyText.getBytes();
        var invalidKey = new SecretKeySpec(invalidKeyBytes, "AES");


        Assertions.assertThrows(InvalidKeyException.class, () -> this.aesEncryptionService.encrypt("thing", invalidKey, this.iv));
        Assertions.assertThrows(InvalidKeyException.class, () -> this.aesEncryptionService.encrypt("thing", null, this.iv));
    }

}
