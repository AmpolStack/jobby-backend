package com.jobby.authorization.infraestructure.adapters.out.encrypt;

import com.jobby.authorization.application.ports.out.encrypt.EncryptionService;
import com.jobby.authorization.domain.result.Error;
import com.jobby.authorization.domain.result.ErrorType;
import com.jobby.authorization.domain.result.Field;
import com.jobby.authorization.domain.result.Result;
import com.jobby.authorization.infraestructure.config.EncryptConfig;
import org.springframework.stereotype.Service;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;

@Service
public class AESEncryptionService implements EncryptionService {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int[] VALID_T_LENGTHS_BITS = new int[] {98, 112, 120, 128};
    private static final int[] VALID_KEY_LENGTHS_BITS = new int[] {128, 192, 256};

    private final DefaultEncryptBuilder encryptBuilder;

    public AESEncryptionService(DefaultEncryptBuilder defaultEncryptBuilder) {
        this.encryptBuilder = defaultEncryptBuilder;
    }

    private Result<Key, Error> validateAndParseKey(String keyBase64){
        if(keyBase64 == null || keyBase64.isBlank()) {
            return Result.failure(ErrorType.ITN_INVALID_OPTION_PARAMETER,
                    new Field(
                            "keyBase64",
                            "Encryption key cannot be empty"
                    )
            );
        }

        var key = EncryptUtils.ParseKeySpec(ALGORITHM, keyBase64);

        if(key == null){
            return Result.failure(ErrorType.ITN_INVALID_OPTION_PARAMETER,
                    new Field(
                            "keyBase64",
                            "Invalid Base64 encoded key or incompatible key format"
                    )
            );
        }

        var keyLengthInBytes = key.getEncoded().length * 8;
        if (Arrays.stream(VALID_KEY_LENGTHS_BITS).noneMatch(length -> length == keyLengthInBytes)) {
            return Result.failure(ErrorType.ITN_INVALID_OPTION_PARAMETER,
                    new Field(
                            "keyBase64",
                            "Key length must be between 16 and 32 bytes for AES encryption"
                    )
            );
        }

        return Result.success(key);
    }

    private Result<Void, Error> validateIvLength(int ivLength){
        if(ivLength <= 0 || ivLength > 16){
            return Result.failure(ErrorType.ITN_INVALID_OPTION_PARAMETER,
                    new Field(
                            "ivLength",
                            "IV length must be between 1 and 16 bytes"
                    )
            );
        }
        return Result.success(null);
    }

    private Result<Void, Error> validateTLen(int tLen){
        if(Arrays.stream(VALID_T_LENGTHS_BITS).noneMatch(length -> length == tLen)){
            return Result.failure(ErrorType.ITN_INVALID_OPTION_PARAMETER,
                    new Field("tLen", "TLen are invalid"));
        }
        return Result.success(null);
    }

    private Result<Void, Error> validateConfig(EncryptConfig encryptConfig){
        if(encryptConfig == null){
            return Result.failure(ErrorType.ITN_INVALID_OPTION_PARAMETER,
                    new Field("encryptConfig", "Encryption config cannot be null")
            );
        }
        if(encryptConfig.getIv() == null){
            return Result.failure(ErrorType.ITN_INVALID_OPTION_PARAMETER,
                    new Field("encryptConfig", "Iv cannot be null")
            );
        }
        return Result.success(null);
    }

    @Override
    public Result<String, Error> encrypt(String data, EncryptConfig config) {
        return validateConfig(config)
                .flatMap(x -> validateIvLength(config.getIv().getLength()))
                .flatMap(x -> validateTLen(config.getIv().getTLen()))
                .flatMap(x -> validateAndParseKey(config.getSecretKey()))
                .flatMap((key -> {
                    var iv = EncryptUtils.generateIv(config.getIv().getLength(), config.getIv().getTLen());
                    return this.encryptBuilder
                            .setData(data.getBytes(StandardCharsets.UTF_8))
                            .setIv(iv)
                            .setKey(key)
                            .setMode(Cipher.ENCRYPT_MODE)
                            .setTransformation(TRANSFORMATION)
                            .build()
                            .map(cipherBytes ->{
                                var buffer = ByteBuffer.allocate(iv.getIV().length + cipherBytes.length);
                                buffer.put(iv.getIV());
                                buffer.put(cipherBytes);
                                var combined = buffer.array();
                                return Base64.getEncoder().encodeToString(combined);
                            });
                }));
    }

    private Result<byte[], Error> validateAndParseCipherText(String cipherText){
        if(cipherText == null ||  cipherText.isBlank()){
            return Result.failure(ErrorType.INVALID_INPUT,
                    new Field(
                            "cipherText",
                            "Cipher text cannot be null or blank"
                    )
            );
        }

        byte[] combined;
        try{
            combined = Base64.getDecoder().decode(cipherText);
        }
        catch (IllegalArgumentException e){
            return Result.failure(ErrorType.INVALID_INPUT,
                    new Field(
                            "cipherText",
                            "Invalid Base64 encoded cipher text"
                    )
            );
        }
        return Result.success(combined);
    }

    private Result<Void, Error> validateCombinedAndIvLength(int ivLength, int combinedLength){
        if(ivLength > combinedLength){
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field(
                            "cipherText",
                            "Cipher text is too short to contain valid data (expected at least " + ivLength + " bytes)"
                    )
            );
        }
        return Result.success(null);
    }

    @Override
    public Result<String, Error> decrypt(String cipherText, EncryptConfig config) {
        return  validateConfig(config)
                .flatMap(x -> validateTLen(config.getIv().getTLen()))
                .flatMap(x -> validateAndParseCipherText(cipherText))
                .flatMap(combined -> validateCombinedAndIvLength(config.getIv().getLength(), combined.length)
                        .flatMap(n -> validateAndParseKey(config.getSecretKey()))
                        .flatMap( key -> {

                            var rawIv = Arrays.copyOfRange(combined, 0, config.getIv().getLength());
                            var data = Arrays.copyOfRange(combined, config.getIv().getLength(), combined.length);
                            var iv = new GCMParameterSpec(config.getIv().getTLen(), rawIv);
                            return this.encryptBuilder
                                    .setData(data)
                                    .setIv(iv)
                                    .setKey(key)
                                    .setMode(Cipher.DECRYPT_MODE)
                                    .setTransformation(TRANSFORMATION)
                                    .build()
                                    .map(bytesResp -> new String(bytesResp, StandardCharsets.UTF_8));
                        })
                );
    }
}
