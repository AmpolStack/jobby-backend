package com.jobby.authorization.infraestructure.adapters.out.hashing.mac;

import com.jobby.authorization.TestAssertions;
import com.jobby.domain.mobility.error.ErrorType;
import com.jobby.infraestructure.adapter.encrypt.EncryptUtils;
import com.jobby.infraestructure.adapter.hashing.mac.DefaultMacBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class DefaultMacBuilderTest {

    private final DefaultMacBuilder macBuilder = new DefaultMacBuilder();

    private Key VALID_SECRET_KEY;
    private String VALID_ALGORITHM;

    @BeforeEach
    public void setup() throws NoSuchAlgorithmException {
        VALID_ALGORITHM = "HmacSHA256";
        VALID_SECRET_KEY = EncryptUtils.generateKey(VALID_ALGORITHM, 256);
    }

    @Test
    public void build_whenInvalidAlgorithm_thenReturnFailure() throws NoSuchAlgorithmException {
        // Arrange
        String invalidAlgorithm = "InvalidAlgorithm";

        // Act
        var result = this.macBuilder
                .setAlgorithm(invalidAlgorithm)
                .setData("data".getBytes())
                .setKey(VALID_SECRET_KEY)
                .build();

        // Assert
        TestAssertions.assertFailure(result, ErrorType.ITS_INVALID_OPTION_PARAMETER,
                "algorithm",
                "Invalid MAC algorithm: " + invalidAlgorithm);

    }

    @Test
    public void build_whenAlgorithmIsNull_thenReturnFailure() throws NoSuchAlgorithmException {

        // Act
        var result = this.macBuilder
                .setAlgorithm(null)
                .setData("data".getBytes())
                .setKey(VALID_SECRET_KEY)
                .build();

        // Assert
        TestAssertions.assertFailure(result, ErrorType.ITS_INVALID_OPTION_PARAMETER,
                "algorithm",
                "MAC algorithm is null");

    }


}
