package com.jobby.authorization.infraestructure.adapters.out.encrypt;

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
        catch (Exception e){
            return null;
        }

        if (bytes.length < 16 || bytes.length > 32) {
            return null;
        }

        return new SecretKeySpec(bytes, algorithm);
    }

    public static GCMParameterSpec generateIv(int ivSize, int keySize){
        var iv = new byte[ivSize];
        new SecureRandom().nextBytes(iv);
        return new GCMParameterSpec(keySize, iv);
    }
}
