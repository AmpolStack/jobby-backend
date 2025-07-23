package com.jobby.authorization.infraestructure.adapters.out;

import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class AESGenerators {
    public static SecretKey generateKey(String algorithm) throws NoSuchAlgorithmException {
        var keyGenerator = KeyGenerator.getInstance(algorithm);
        // normally 128, 192 or 256 bits
        keyGenerator.init(128);
        return keyGenerator.generateKey();
    }

    public static GCMParameterSpec generateIv(int ivSize, int keySize){
        var iv = new byte[ivSize];
        new SecureRandom().nextBytes(iv);
        return new GCMParameterSpec(keySize, iv);
    }
}
