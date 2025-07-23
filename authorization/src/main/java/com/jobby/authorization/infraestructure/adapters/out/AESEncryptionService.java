package com.jobby.authorization.infraestructure.adapters.out;

import com.jobby.authorization.application.ports.out.EncryptionService;
import com.jobby.authorization.infraestructure.config.EncryptConfig;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

@Component
public class AESEncryptionService implements EncryptionService {

    private final EncryptConfig encryptConfig;

    public AESEncryptionService(EncryptConfig config) {
        this.encryptConfig = config;
    }


    @Override
    public String encrypt(String plainText)
            throws NoSuchPaddingException,
            NoSuchAlgorithmException,
            InvalidAlgorithmParameterException,
            InvalidKeyException,
            IllegalBlockSizeException,
            BadPaddingException {
        var cipher = Cipher.getInstance(encryptConfig.getInstance().getComplexName());

        var keyRaw = Base64.getDecoder().decode(encryptConfig.getSecretKey().getValue());
        var key = new SecretKeySpec(keyRaw, encryptConfig.getInstance().getSimpleName());

        var iv = AESGenerators.generateIv(encryptConfig.getIv().getLength(), encryptConfig.getSecretKey().getLength());

        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        var cipherText = cipher.doFinal(plainText.getBytes());

        var buffer = ByteBuffer.allocate(iv.getIV().length + cipherText.length);
        buffer.put(iv.getIV());
        buffer.put(cipherText);
        var combined = buffer.array();

        return Base64.getEncoder().encodeToString(combined);
    }

    @Override
    public String decrypt(String cipherText)
            throws NoSuchPaddingException,
            NoSuchAlgorithmException,
            InvalidAlgorithmParameterException,
            InvalidKeyException,
            IllegalBlockSizeException,
            BadPaddingException {
        var combined = Base64.getDecoder().decode(cipherText);
        var rawIv = Arrays.copyOfRange(combined, 0, encryptConfig.getIv().getLength());
        var rawData = Arrays.copyOfRange(combined, encryptConfig.getIv().getLength(), combined.length);

        var cipher = Cipher.getInstance(encryptConfig.getInstance().getComplexName());
        var iv = new GCMParameterSpec(encryptConfig.getSecretKey().getLength(), rawIv);

        var keyRaw = Base64.getDecoder().decode(encryptConfig.getSecretKey().getValue());
        var key = new SecretKeySpec(keyRaw, encryptConfig.getInstance().getSimpleName());
        cipher.init(Cipher.DECRYPT_MODE, key, iv);

        var plainText = cipher.doFinal(rawData);
        return new String(plainText);
    }
}