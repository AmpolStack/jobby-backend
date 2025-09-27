package com.jobby.infraestructure.adapter.encrypt;

import com.jobby.domain.configurations.MacConfig;
import com.jobby.domain.ports.MacService;
import com.jobby.domain.ports.SafeResultValidator;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.error.ErrorType;
import com.jobby.domain.mobility.error.Field;
import com.jobby.domain.mobility.result.Result;
import com.jobby.domain.mobility.validator.ValidationChain;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

public class HmacSha256Service implements MacService {
    private static final String ALGORITHM = "HmacSHA256";
    private static final Integer[] VALID_KEY_LENGTHS_BITS = {128, 160, 192, 224, 256, 384, 512};

    private final SafeResultValidator validator;
    private final DefaultMacBuilder hmacBuilder;

    public HmacSha256Service(SafeResultValidator validator, DefaultMacBuilder hmacBuilder) {
        this.validator = validator;
        this.hmacBuilder = hmacBuilder;
    }

    @Override
    public Result<String, Error> generateMac(String data, MacConfig config) {
        return validateConfig(config)
                .flatMap(v -> validateAndParseKey(config.getSecretKey()))
                .flatMap(key -> ValidationChain.create()
                        .validateInternalNotBlank(data, "data")
                        .build()
                        .flatMap(v -> hmacBuilder
                                .setData(data.getBytes(StandardCharsets.UTF_8))
                                .setKey(key)
                                .setAlgorithm(config.getAlgorithm())
                                .build()
                                .map(hmacBytes -> Base64.getEncoder().encodeToString(hmacBytes))
                        )
                );
    }

    @Override
    public Result<Boolean, Error> verifyMac(String data, String hmac, MacConfig config) {
        return generateMac(data, config)
                .flatMap(generatedHmac -> {
                    boolean isValid = generatedHmac.equals(hmac);
                    return Result.success(isValid);
                });
    }

    private Result<Void, Error> validateConfig(MacConfig config) {
        return ValidationChain.create()
                .add(this.validator.validate(config))
                .validateInternalAnyMatch(
                        config.getAlgorithm(),
                        new String[]{ALGORITHM, "HmacSHA1", "HmacSHA512"},
                        "algorithm")
                .build();
    }

    private Result<Key, Error> validateAndParseKey(String keyBase64) {
        final int BIT_MULTIPLIER = 8;
        return ValidationChain.create()
                .validateInternalNotBlank(keyBase64, "key-base-64")
                .build()
                .flatMap(v -> {
                    byte[] keyBytes;
                    try {
                        keyBytes = Base64.getDecoder().decode(keyBase64);
                    } catch (IllegalArgumentException e) {
                        return Result.failure(ErrorType.ITS_SERIALIZATION_ERROR,
                                new Field("key-base-64", "is invalid in base64"));
                    }

                    var keyLengthInBits = keyBytes.length * BIT_MULTIPLIER;

                    return ValidationChain.create()
                            .validateInternalAnyMatch(keyLengthInBits, VALID_KEY_LENGTHS_BITS, "key-base-64-bits")
                            .build()
                            .map(v2 -> new SecretKeySpec(keyBytes, ALGORITHM));
                });
    }
}
