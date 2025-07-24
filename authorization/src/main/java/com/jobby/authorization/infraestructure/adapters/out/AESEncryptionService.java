package com.jobby.authorization.infraestructure.adapters.out;

import com.jobby.authorization.application.ports.out.EncryptionService;
import com.jobby.authorization.domain.result.Error;
import com.jobby.authorization.domain.result.ErrorType;
import com.jobby.authorization.domain.result.Field;
import com.jobby.authorization.domain.result.Result;
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
    public Result<String, Error> encrypt(String plainText) {

        Cipher cipher;
        var complexName = this.encryptConfig.getInstance().getComplexName();
        try{
            cipher = Cipher.getInstance(complexName);
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException e){
            return Result.failure(
                    ErrorType.ITN_INVALID_OPTION_PARAMETER,
                    new Field(
                            "encrypt.instance.complex-name",
                            "the value: [ " + complexName + " ] complex-name are invalid or null"
                    )
            );
        }

        byte[] keyRaw;
        var keyValue = this.encryptConfig.getSecretKey().getValue();
        try{
            keyRaw = Base64.getDecoder().decode(keyValue);
        }
        catch (IllegalArgumentException e){
            return Result.failure(
                    ErrorType.ITN_INVALID_OPTION_PARAMETER,
                    new Field(
                            "encrypt.key.value",
                            "the value: [ " + keyValue + " ] value are invalid or null"
                    )
            );
        }

        var key = new SecretKeySpec(keyRaw, encryptConfig.getInstance().getSimpleName());

        // Should never return an exception
        var iv = AESGenerators.generateIv(encryptConfig.getIv().getLength(), encryptConfig.getSecretKey().getLength());

        try {
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        }
        catch (InvalidKeyException | InvalidAlgorithmParameterException e){
            return Result.failure(
                    ErrorType.ITN_OPERATION_ERROR,
                    new Field[]{
                            new Field(
                                    "encrypt.key.value",
                                    "the value : [ " + key + " ] key are probably incompatible or null, AES encryption failed"
                            ),
                            new Field(
                                    "iv",
                                    "the value : [ " + key + " ] iv are probably incompatible or null, AES encryption failed"
                            ),
                    }
            );
        }

        byte[] cipherBytes;
        try{
            cipherBytes = cipher.doFinal(plainText.getBytes());
        }
        catch (IllegalBlockSizeException | BadPaddingException e){
            return Result.failure(
                    ErrorType.ITN_OPERATION_ERROR,
                    new Field(
                            "cipher.doFinal",
                            "The operation returned an error, probably because one byte are invalid or corrupted"
                    )
            );
        }

        var buffer = ByteBuffer.allocate(iv.getIV().length + cipherBytes.length);
        buffer.put(iv.getIV());
        buffer.put(cipherBytes);
        var combined = buffer.array();

        var response = Base64.getEncoder().encodeToString(combined);
        return Result.success(response);
    }

    @Override
    public Result<String, Error> decrypt(String cipherText)
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