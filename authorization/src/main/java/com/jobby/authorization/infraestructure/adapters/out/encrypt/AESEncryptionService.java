package com.jobby.authorization.infraestructure.adapters.out.encrypt;

import com.jobby.authorization.domain.ports.out.SafeResultValidator;
import com.jobby.authorization.domain.ports.out.encrypt.EncryptionService;
import com.jobby.domain.mobility.Error;
import com.jobby.domain.mobility.ErrorType;
import com.jobby.domain.mobility.Field;
import com.jobby.domain.result.Result;
import com.jobby.authorization.domain.shared.validators.ValidationChain;
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
    private static final Integer[] VALID_T_LENGTHS_BITS = {98, 112, 120, 128};
    private static final Integer[] VALID_KEY_LENGTHS_BITS = {128, 192, 256};

    private final SafeResultValidator validator;
    private final DefaultEncryptBuilder encryptBuilder;

    public AESEncryptionService(SafeResultValidator validator, DefaultEncryptBuilder defaultEncryptBuilder) {
        this.validator = validator;
        this.encryptBuilder = defaultEncryptBuilder;
    }

    @Override
    public Result<String, Error> decrypt(String cipherText, EncryptConfig config) {
        return  validateConfig(config)
                .flatMap(v -> validateAndParseCipherText(cipherText))
                .flatMap(combined -> ValidationChain.create()
                        .validateInternalGreaterThan(
                                combined.length, config.getIv().getLength(),
                                "combined")
                        .build()
                        .flatMap(v -> validateAndParseKey(config.getSecretKey()))
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

    @Override
    public Result<String, Error> encrypt(String data, EncryptConfig config) {
        return validateConfig(config)
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


    private Result<Void, Error> validateConfig(EncryptConfig config) {
        return ValidationChain.create()
                .add(this.validator.validate(config))
                .validateInternalAnyMatch(
                        config.getIv().getTLen(),
                        VALID_T_LENGTHS_BITS,
                        "t-len")
                .build();
    }

    private Result<Key, Error> validateAndParseKey(String keyBase64){
        final int BIT_MULTIPLIER = 8;
        return ValidationChain.create()
                .validateInternalNotBlank(keyBase64, "key-base-64")
                .build()
                .flatMap(v -> {
                    var key = EncryptUtils.ParseKeySpec(ALGORITHM, keyBase64);

                    if(key == null){
                        return Result.failure(ErrorType.ITS_SERIALIZATION_ERROR,
                                new Field("key-base-64", "is invalid in base64"));
                    }

                    var keyLengthInBytes = key.getEncoded().length * BIT_MULTIPLIER;

                    return ValidationChain.create()
                            .validateInternalAnyMatch(keyLengthInBytes, VALID_KEY_LENGTHS_BITS, "key-base-64-bytes")
                            .build()
                            .map(v3 -> key);
                });
    }

    private Result<byte[], Error> validateAndParseCipherText(String cipherText){
        return ValidationChain.create()
                .validateInternalNotBlank(cipherText, "cipher-text")
                .build()
                .flatMap(v -> {
                    byte[] combined;
                    try{
                        combined = Base64.getDecoder().decode(cipherText);
                    }
                    catch (IllegalArgumentException e){
                        return Result.failure(ErrorType.ITS_SERIALIZATION_ERROR,
                                new Field(
                                        "cipherText",
                                        "Invalid Base64 encoded cipher text"
                                )
                        );
                    }
                    return Result.success(combined);
                });
    }

}
