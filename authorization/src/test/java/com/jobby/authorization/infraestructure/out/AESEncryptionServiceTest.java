//package com.jobby.authorization.infraestructure.out;
//
//import com.jobby.authorization.infraestructure.adapters.out.AESEncryptionService;
//import com.jobby.authorization.infraestructure.adapters.out.AESGenerators;
//import com.jobby.authorization.infraestructure.config.EncryptConfig;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.ValueSource;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import javax.crypto.BadPaddingException;
//import javax.crypto.IllegalBlockSizeException;
//import javax.crypto.NoSuchPaddingException;
//import javax.crypto.SecretKey;
//import java.security.InvalidAlgorithmParameterException;
//import java.security.InvalidKeyException;
//import java.security.NoSuchAlgorithmException;
//import java.util.Base64;
//
//@ExtendWith(MockitoExtension.class)
//public class AESEncryptionServiceTest {
//
//    private EncryptConfig defaultConfig = new EncryptConfig(
//            new EncryptConfig.Instance("AES", "AES/GCM/NoPadding"),
//            new EncryptConfig.Iv(12),
//            new EncryptConfig.SecretKey(getNewKey(),128)
//    );
//
//    @Mock
//    private EncryptConfig encryptConfig;
//
//    @InjectMocks
//    private AESEncryptionService aesEncryptionService;
//
//    @BeforeEach
//    public void beforeEach(){
//        var key = getNewKey();
//        this.defaultConfig.getSecretKey().setValue(key);
//    }
//
//    @ParameterizedTest
//    @ValueSource(strings = {
//            "privateData",
//            "privatedata@gmail.com",
//            "+12 431-213-21",
//            "CCV-340",
//            "{ secure: {address: 12-2, phone: 214-33}, admin: false}"})
//    public void encryptAndDecrypt_oneInput_WhenInputIsValid(String value) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
//        // Act
////        var encryptText = this.aesEncryptionService.encrypt(value);
////        var decryptText = this.aesEncryptionService.decrypt(encryptText.getData());
////
////        // Assert
////        Assertions.assertEquals(value, decryptText);
//    }
//
//    @Test
//    public void encrypt_WhenInputIsNull_ThenExceptionIsThrown() {
//        // Act & Assert
////        Assertions.assertThrows(NullPointerException.class, () -> aesEncryptionService.encrypt(null));
//    }
//
//    @Test
//    public void encrypt_WhenSecretKeyIsNull_ThenExceptionIsThrown() {
//        // Act
//        Mockito.when(encryptConfig.getSecretKey()).thenReturn(this.defaultConfig.getSecretKey());
//        Mockito.when(encryptConfig.getIv()).thenReturn(this.defaultConfig.getIv());
//        Mockito.when(encryptConfig.getInstance()).thenReturn(this.defaultConfig.getInstance());
//
//        var actionResult = this.aesEncryptionService.encrypt("thing");
//        //Assert
//        Assertions.assertTrue(actionResult.isFailure());
//    }
//
//    @Test
//    public void encrypt_WhenSecretKeyIsInvalid_ThenExceptionIsThrown() {
////        // Act & Assert
////        var invalidKeyText = "invalidKeyText";
////        // If the key byte array length is less than 128 it is invalid.
////        // in this example the length equals 14
////        var invalidKeyBytes = invalidKeyText.getBytes();
////        var invalidKey = Base64.getEncoder().encodeToString(invalidKeyBytes);
////
////        this.encryptConfig.getSecretKey().setValue(invalidKey);
////
////        Assertions.assertThrows(InvalidKeyException.class, () -> aesEncryptionService.encrypt("thing"));
//    }
//
//    private static String getNewKey(){
//        SecretKey key;
//        try{
//            key = AESGenerators.generateKey("AES");
//        }
//        catch (NoSuchAlgorithmException e){
//            return null;
//        }
//        var keyBytes = key.getEncoded();
//        return Base64.getEncoder().encodeToString(keyBytes);
//    }
//
//
//}
