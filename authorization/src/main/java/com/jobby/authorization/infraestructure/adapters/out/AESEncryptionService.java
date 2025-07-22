package com.jobby.authorization.infraestructure.adapters.out;

import com.jobby.authorization.application.ports.out.EncryptionService;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

@Component
public class AESEncryptionService implements EncryptionService {
    @Override
    public String encrypt(String plainText, SecretKey key)
            throws NoSuchPaddingException,
            NoSuchAlgorithmException,
            InvalidAlgorithmParameterException,
            InvalidKeyException,
            IllegalBlockSizeException,
            BadPaddingException {
        var cipher = Cipher.getInstance("AES/GCM/NoPadding");
        var iv = AESGenerators.generateIv();
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        var cipherText = cipher.doFinal(plainText.getBytes());

        var buffer = ByteBuffer.allocate(iv.getIV().length + cipherText.length);
        buffer.put(iv.getIV());
        buffer.put(cipherText);
        var combined = buffer.array();

        return Base64.getEncoder().encodeToString(combined);
    }

    @Override
    public String decrypt(String cipherText, SecretKey key)
            throws NoSuchPaddingException,
            NoSuchAlgorithmException,
            InvalidAlgorithmParameterException,
            InvalidKeyException,
            IllegalBlockSizeException,
            BadPaddingException {
        var combined = Base64.getDecoder().decode(cipherText);
        var rawIv = Arrays.copyOfRange(combined, 0, 12);
        var rawData = Arrays.copyOfRange(combined, 12, combined.length);

        var cipher = Cipher.getInstance("AES/GCM/NoPadding");
        var iv = new GCMParameterSpec(128, rawIv);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);

        var plainText = cipher.doFinal(Base64.getDecoder().decode(rawData));
        return new String(plainText);
    }
}