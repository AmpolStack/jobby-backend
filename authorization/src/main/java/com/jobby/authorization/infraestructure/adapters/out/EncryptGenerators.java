package com.jobby.authorization.infraestructure.adapters.out;

import com.jobby.authorization.domain.result.Error;
import com.jobby.authorization.domain.result.ErrorType;
import com.jobby.authorization.domain.result.Field;
import com.jobby.authorization.domain.result.Result;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class EncryptGenerators {
    public SecretKey generateKey(String algorithm) throws NoSuchAlgorithmException {
        var keyGenerator = KeyGenerator.getInstance(algorithm);
        // normally 128, 192 or 256 bits
        keyGenerator.init(128);
        return keyGenerator.generateKey();
    }

    public SecretKeySpec ParseKeySpec(String algorithm, String valueBase64) {
        if (valueBase64 == null || valueBase64.isBlank()) {
            return null;
        }
        
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

    public Result<byte[], Error> runAlgorithm(byte[] data,
                                              SecretKeySpec key,
                                              GCMParameterSpec iv,
                                              int mode,
                                              String instance) {
        Cipher cipher;
        try{
            cipher = Cipher.getInstance(instance);
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException e){
            return Result.failure(ErrorType.ITN_INVALID_OPTION_PARAMETER,
                    new Field(
                            "transformation",
                            "Invalid cipher transformation: " + instance
                    )
            );
        }

        try {
            cipher.init(mode, key, iv);
        }
        catch (InvalidKeyException | InvalidAlgorithmParameterException e){
            return Result.failure(ErrorType.ITN_OPERATION_ERROR,
                    new Field(
                            "cipher",
                            "Failed to initialize cipher with provided key and IV"
                    )
            );
        }

        byte[] cipherBytes;
        try{
            cipherBytes = cipher.doFinal(data);
        }
        catch (IllegalBlockSizeException | BadPaddingException e){
            return Result.failure(ErrorType.ITN_OPERATION_ERROR,
                    new Field(
                            "data",
                            "Cipher operation failed - data may be corrupted or incompatible"
                    )
            );
        }

        return Result.success(cipherBytes);
    }

    public GCMParameterSpec generateIv(int ivSize, int keySize){
        var iv = new byte[ivSize];
        new SecureRandom().nextBytes(iv);
        return new GCMParameterSpec(keySize, iv);
    }
}
