package com.jobby.authorization.infraestructure.adapters.out.encrypt;

import com.jobby.authorization.domain.ports.out.SafeResultValidator;
import com.jobby.authorization.domain.ports.out.encrypt.EncryptionService;
import com.jobby.authorization.domain.shared.validators.NumberValidator;
import com.jobby.authorization.domain.shared.validators.ObjectValidator;
import com.jobby.authorization.domain.shared.validators.StringValidator;
import com.jobby.authorization.domain.shared.result.Error;
import com.jobby.authorization.domain.shared.result.ErrorType;
import com.jobby.authorization.domain.shared.result.Field;
import com.jobby.authorization.domain.shared.result.Result;
import com.jobby.authorization.infraestructure.config.EncryptConfig;
import org.springframework.stereotype.Service;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

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
        return  this.validator.validate(config)
                .flatMap(v -> ObjectValidator.validateAnyMatch(config.getIv().getTLen(), VALID_T_LENGTHS_BITS, "tLen"))
                .flatMap(x -> validateAndParseKey(config.getSecretKey()))
                .flatMap(v -> validateAndParseCipherText(cipherText))
                .flatMap(combined -> NumberValidator.validateGreaterInteger(config.getIv().getLength(), combined.length, "iv-length")
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
        return this.validator.validate(config)
                .flatMap(x -> ObjectValidator.validateAnyMatch(config.getIv().getTLen(), VALID_T_LENGTHS_BITS, "tLen"))
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

    private Result<Key, Error> validateAndParseKey(String keyBase64){
        final int BIT_MULTIPLIER = 8;
        return StringValidator.validateNotBlankString(keyBase64, "keyBase64")
                .flatMap(v -> {
                    var key = EncryptUtils.ParseKeySpec(ALGORITHM, keyBase64);

                    if(key == null){
                        return Result.failure(ErrorType.ITN_SERIALIZATION_ERROR,
                                new Field("keyBase64", "is invalid in base64"));
                    }

                    var keyLengthInBytes = Objects.requireNonNull(key).getEncoded().length * BIT_MULTIPLIER;
                    return ObjectValidator.validateAnyMatch(keyLengthInBytes, VALID_KEY_LENGTHS_BITS, "keyBase64-bytes")
                            .map(v3 -> key);
                });
    }

    private Result<byte[], Error> validateAndParseCipherText(String cipherText){
        return StringValidator.validateNotBlankString(cipherText, "cipher-text")
                .flatMap(v -> {
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
                });
    }

}
