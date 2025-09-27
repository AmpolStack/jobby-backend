package com.jobby.authorization.infraestructure.adapters.out.hashing.mac;

import com.jobby.authorization.TestAssertions;
import com.jobby.domain.mobility.error.ErrorType;
import com.jobby.infraestructure.adapter.encrypt.EncryptUtils;
import com.jobby.infraestructure.adapter.hashing.mac.DefaultMacBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertThrows;

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
    public void build_whenInvalidAlgorithm_thenReturnFailure(){
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
    public void build_whenAlgorithmIsNull_thenReturnNullPointerException(){

        // Act
        var result = this.macBuilder
                .setAlgorithm(null)
                .setData("data".getBytes())
                .setKey(VALID_SECRET_KEY);

        // Assert
        assertThrows(NullPointerException.class, result::build);
    }

    @Test
    public void build_whenKeyIsNull_thenReturnFailure() {
        // Act
        var result = this.macBuilder
                .setAlgorithm(VALID_ALGORITHM)
                .setData("data".getBytes())
                .setKey(null)
                .build();

        // Assert
        TestAssertions.assertFailure(result, ErrorType.ITS_OPERATION_ERROR,
                "key",
                "Invalid key for MAC: null");
    }

    @Test
    public void build_whenDataIsNull_thenReturnSuccess() {
        // Act
        var result = this.macBuilder
                .setAlgorithm(VALID_ALGORITHM)
                .setData(null)
                .setKey(VALID_SECRET_KEY)
                .build();

        // Assert
        TestAssertions.assertSuccess(result);
    }

    @RepeatedTest(100)
    public void build_whenAllParametersAreValid_thenReturnSuccess() {
        // Arrange
        String data = "data";

        // Act
        var result = this.macBuilder
                .setAlgorithm(VALID_ALGORITHM)
                .setData(data.getBytes())
                .setKey(VALID_SECRET_KEY)
                .build();

        // Assert
        TestAssertions.assertSuccess(result);
    }


    @RepeatedTest(100)
    public void build_whenEquals(){
        // Arrange
        String data = "data";

        // Act
        var result1 = this.macBuilder
                .setAlgorithm(VALID_ALGORITHM)
                .setData(data.getBytes())
                .setKey(VALID_SECRET_KEY)
                .build()
                .map(x -> Base64.getEncoder().encodeToString(x));

        var result2 = this.macBuilder
                .setAlgorithm(VALID_ALGORITHM)
                .setData(data.getBytes())
                .setKey(VALID_SECRET_KEY)
                .build()
                .map(x -> Base64.getEncoder().encodeToString(x));



        // Assert
        TestAssertions.assertSuccess(result1);
        TestAssertions.assertSuccess(result2);
        Assertions.assertEquals(result1, result2);
    }

}
