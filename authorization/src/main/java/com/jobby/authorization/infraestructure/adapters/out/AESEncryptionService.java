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

    private final EncryptConfig config;

    public AESEncryptionService(EncryptConfig config) {
        this.config = config;
    }

    @Override
    public Result<String, Error> encrypt(String plainText) {

        var complexName = this.config.getInstance().getComplexName();
        var cipher = getCipher(complexName);
        if(cipher == null){
            return Result.failure(
                    ErrorType.ITN_INVALID_OPTION_PARAMETER,
                    new Field(
                            "encrypt.instance.complex-name",
                            "the value: [ " + complexName + " ] are invalid or null"
                    )
            );
        }

        var keyValue = this.config.getSecretKey().getValue();
        var keyRaw = decode(keyValue);
        if(keyRaw == null){
            return Result.failure(
                    ErrorType.ITN_INVALID_OPTION_PARAMETER,
                    new Field(
                            "encrypt.key.value",
                            "the value: [ " + keyValue + " ] are invalid or null"
                    )
            );
        }

        var key = new SecretKeySpec(keyRaw, config.getInstance().getSimpleName());
        // Should never return an exception
        var iv = AESGenerators.generateIv(config.getIv().getLength(), config.getSecretKey().getLength());

        if(initCipher(cipher, Cipher.ENCRYPT_MODE, key, iv)){
            return Result.failure(
                    ErrorType.ITN_OPERATION_ERROR,
                    new Field[]{
                            new Field(
                                    "encrypt.key.value",
                                    "the value : [ " + key + " ] are probably incompatible or null, AES encryption failed"
                            ),
                            new Field(
                                    "iv",
                                    "the value : [ " + key + " ] are probably incompatible or null, AES encryption failed"
                            ),
                    }
            );
        }

        var cipherBytes = doFinal(cipher, plainText);
        if(cipherBytes == null){
            return Result.failure(
                    ErrorType.INVALID_INPUT,
                    new Field(
                            "plainText",
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
    public Result<String, Error> decrypt(String cipherText) {

        if(cipherText.isBlank()){
            return Result.failure(
                    ErrorType.INVALID_INPUT,
                    new Field(
                            "cipherText",
                            "the input are null"
                    )
            );
        }

        var combined = decode(cipherText);
        if(combined == null){
            return Result.failure(
                    ErrorType.INVALID_INPUT,
                    new Field(
                            "cipherText",
                            "the value [ " + cipherText + " ] are invalid for decode in Base64 format"
                    )
            );
        }

        var ivLength = this.config.getIv().getLength();
        if(ivLength > combined.length){
            return Result.failure(
                    ErrorType.VALIDATION_ERROR,
                    new Field(
                            "cipherText",
                            "the value: [" + cipherText + "] are invalid or null"
                    )
            );
        }

        var rawIv = copyOfRange(combined, 0, ivLength);
        if(rawIv == null){
            return Result.failure(
                    ErrorType.ITN_OPERATION_ERROR,
                    new Field(
                            "rawIv.length",
                            "the value [" + ivLength + "] are invalid"
                    )
            );
        }

        var rawData = copyOfRange(combined, ivLength, combined.length);
        if(rawData == null){
            return Result.failure(
                    ErrorType.ITN_OPERATION_ERROR,
                    new Field(
                            "rawIv.length",
                            "the value [" + ivLength + "] are invalid"
                    )
            );
        }

        var complexName = this.config.getInstance().getComplexName();
        var cipher = getCipher(complexName);
        if(cipher == null){
            return Result.failure(
                    ErrorType.ITN_INVALID_OPTION_PARAMETER,
                    new Field(
                            "encrypt.instance.complex-name",
                            "the value: [ " + complexName + " ] are invalid or null"
                    )
            );
        }

        var iv = new GCMParameterSpec(config.getSecretKey().getLength(), rawIv);

        var keyRaw = decode(config.getSecretKey().getValue());
        if(keyRaw == null){
            return Result.failure(
                    ErrorType.ITN_OPERATION_ERROR,
                    new Field(
                            "keyRaw",
                            "the value [ " + cipherText + " ] are invalid for decode in Base64 format"
                    )
            );
        }

        var key = new SecretKeySpec(keyRaw, config.getInstance().getSimpleName());

        if(initCipher(cipher, Cipher.DECRYPT_MODE, key, iv)){
            return Result.failure(
                    ErrorType.ITN_OPERATION_ERROR,
                    new Field[]{
                            new Field(
                                    "encrypt.key.value",
                                    "the value : [ " + key + " ] are probably incompatible or null, AES encryption failed"
                            ),
                            new Field(
                                    "iv",
                                    "the value : [ " + key + " ] are probably incompatible or null, AES encryption failed"
                            ),
                    }
            );
        }

        byte[] cipherBytes = doFinal(cipher, rawData);
        if(cipherBytes == null){
            return Result.failure(
                    ErrorType.INVALID_INPUT,
                    new Field(
                            "plainText",
                            "The operation returned an error, probably because one byte are invalid or corrupted"
                    )
            );
        }

        return Result.success(new String(cipherBytes));
    }

    private static Cipher getCipher(String complexName) {
        try{
            return Cipher.getInstance(complexName);
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException e){
            return null;
        }
    }

    private static byte[] decode(String keyValue){
        try{
            return Base64.getDecoder().decode(keyValue);
        }
        catch (IllegalArgumentException e){
            return null;
        }
    }

    private static byte[] copyOfRange(byte[] original, int from, int to){
        try{
            return Arrays.copyOfRange(original, from, to);
        }
        catch (ArrayIndexOutOfBoundsException e){
            return null;
        }
    }

    private static boolean initCipher(Cipher cipher, int mode,
                                      SecretKeySpec key, GCMParameterSpec iv) {
        try {
            cipher.init(mode, key, iv);
            return true;
        }
        catch (InvalidKeyException | InvalidAlgorithmParameterException e){
            return false;
        }
    }

    private static byte[] doFinal(Cipher cipher, String plainText){
        try{
            return cipher.doFinal(plainText.getBytes());
        }
        catch (IllegalBlockSizeException | BadPaddingException e){
            return null;
        }
    }

    private static byte[] doFinal(Cipher cipher, byte[] data){
        try{
            return cipher.doFinal(data);
        }
        catch (IllegalBlockSizeException | BadPaddingException e){
            return null;
        }
    }
}