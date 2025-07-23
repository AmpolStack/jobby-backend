package com.jobby.authorization.infraestructure.out;

import com.jobby.authorization.infraestructure.adapters.out.AESEncryptionService;
import com.jobby.authorization.infraestructure.adapters.out.AESGenerators;
import com.jobby.authorization.infraestructure.config.EncryptConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AESEncryptionServiceTest {

    private AESEncryptionService aesEncryptionService;

    private EncryptConfig encryptConfig;

    @BeforeEach
    public void beforeEach() throws NoSuchAlgorithmException {
        this.encryptConfig = new EncryptConfig();
        this.encryptConfig.setInstance(new EncryptConfig.Instance("AES", "AES/GCM/NoPadding"));
        this.encryptConfig.setIv(new EncryptConfig.Iv(12));

        var key = AESGenerators.generateKey("AES");
        var keyBytes = key.getEncoded();
        var keyString = Base64.getEncoder().encodeToString(keyBytes);

        this.encryptConfig.setSecretKey(new EncryptConfig.SecretKey(keyString, 128));

        this.aesEncryptionService = new AESEncryptionService(encryptConfig);
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
        var encryptText = this.aesEncryptionService.encrypt(value);
        var decryptText = this.aesEncryptionService.decrypt(encryptText);

        // Assert
        Assertions.assertEquals(value, decryptText);
    }

    @Test
    public void encrypt_WhenInputIsNull_ThenExceptionIsThrown() {
        // Act & Assert
        Assertions.assertThrows(NullPointerException.class, () -> aesEncryptionService.encrypt(null));
    }

    @Test
    public void encrypt_WhenSecretKeyIsNull_ThenExceptionIsThrown() {
        // Act & Assert
        this.encryptConfig.getSecretKey().setValue(null);
        Assertions.assertThrows(NullPointerException.class, () -> aesEncryptionService.encrypt("thing"));
    }

    @Test
    public void encrypt_WhenSecretKeyIsInvalid_ThenExceptionIsThrown() {
        // Act & Assert
        var invalidKeyText = "invalidKeyText";
        // If the key byte array length is less than 128 it is invalid.
        // in this example the length equals 14
        var invalidKeyBytes = invalidKeyText.getBytes();
        var invalidKey = Base64.getEncoder().encodeToString(invalidKeyBytes);

        this.encryptConfig.getSecretKey().setValue(invalidKey);

        Assertions.assertThrows(InvalidKeyException.class, () -> aesEncryptionService.encrypt("thing"));
    }

}
