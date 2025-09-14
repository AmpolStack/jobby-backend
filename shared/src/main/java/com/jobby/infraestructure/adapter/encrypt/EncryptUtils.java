package com.jobby.infraestructure.adapter.encrypt;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptUtils {
    public static SecretKey generateKey(String algorithm, int size) throws NoSuchAlgorithmException {
        var keyGenerator = KeyGenerator.getInstance(algorithm);
        // normally 128, 192 or 256 (ideal) bits
        keyGenerator.init(size);
        return keyGenerator.generateKey();
    }

    public static SecretKeySpec ParseKeySpec(String algorithm, String valueBase64) {
        byte[] bytes;
        try{
            bytes = Base64.getDecoder().decode(valueBase64);
        }
        catch (IllegalArgumentException e){
            return null;
        }

        return new SecretKeySpec(bytes, algorithm);
    }

    public static GCMParameterSpec generateIv(int ivSize, int tLen){
        var iv = new byte[ivSize];
        new SecureRandom().nextBytes(iv);
        // Between 98, 112, 120, or 128 (most popular and recommended for NIST)
        return new GCMParameterSpec(tLen, iv);
    }
}
