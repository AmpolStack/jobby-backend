package com.jobby.authorization.infraestructure.adapters.out;

import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class AESGenerators {
    public SecretKey generateKey(String algorithm) throws NoSuchAlgorithmException {
        var keyGenerator = KeyGenerator.getInstance(algorithm);
        // normally 128, 192 or 256 bits
        keyGenerator.init(128);
        return keyGenerator.generateKey();
    }

    public SecretKeySpec generateKeySpec(String algorithm, String valueBase64) {
        byte[] bytes;
        try{
            bytes = Base64.getDecoder().decode(valueBase64);
        }
        catch (Exception e){
            return null;
        }
        return new SecretKeySpec(bytes, algorithm);
    }

    public GCMParameterSpec generateIv(int ivSize, int keySize){
        var iv = new byte[ivSize];
        new SecureRandom().nextBytes(iv);
        return new GCMParameterSpec(keySize, iv);
    }
}
