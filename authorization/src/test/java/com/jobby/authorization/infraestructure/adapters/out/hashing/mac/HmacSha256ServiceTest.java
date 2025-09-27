package com.jobby.authorization.infraestructure.adapters.out.hashing.mac;

import com.jobby.authorization.TestAssertions;
import com.jobby.domain.configurations.MacConfig;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.error.ErrorType;
import com.jobby.domain.mobility.error.Field;
import com.jobby.domain.mobility.result.Result;
import com.jobby.domain.mobility.validator.ValidationChain;
import com.jobby.domain.ports.SafeResultValidator;
import com.jobby.infraestructure.adapter.encrypt.EncryptUtils;
import com.jobby.infraestructure.adapter.hashing.mac.DefaultMacBuilder;
import com.jobby.infraestructure.adapter.hashing.mac.HmacSha256Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

@ExtendWith(MockitoExtension.class)
public class HmacSha256ServiceTest {

    private String VALID_DATA;
    private MacConfig VALID_CONFIG;

    @Mock
    private SafeResultValidator safeResultValidator;

    @Mock
    private DefaultMacBuilder defaultMacBuilder;

    @InjectMocks
    private HmacSha256Service service;

    @BeforeEach
    public void setUp() throws NoSuchAlgorithmException {
        VALID_DATA = "data";
        var VALID_ALGORITHM = "HmacSHA256";
        var VALID_SECRET_KEY = EncryptUtils.generateKey(VALID_ALGORITHM, 256);
        var key = Base64.getEncoder().encodeToString(VALID_SECRET_KEY.getEncoded());
        VALID_CONFIG = new MacConfig(key, VALID_ALGORITHM);

    }


    @Test
    public void generateMac_whenConfigIsNull_thenReturnFailure() {
        // Arrange
        Result<Void, Error> expectedResult = ValidationChain.create()
                .validateInternalNotNull(null, "mac config").build();

        // Act
        var result = this.service.generateMac(VALID_DATA, null);

        // Assert
        TestAssertions.assertFailure(result, Result.renewFailure(expectedResult));
    }

    @Test
    public void generateMac_whenConfigValidationReturnsFailure_thenReturnFailure() {
        // Arrange
        Result<Void, Error> expectedResult = Result.failure(ErrorType.VALIDATION_ERROR,
                new Field("mac config", "expected reason"));

        Mockito.when(this.safeResultValidator.validate(VALID_CONFIG)).thenReturn(expectedResult);

        // Act
        var result = this.service.generateMac(VALID_DATA, VALID_CONFIG);

        // Assert
        TestAssertions.assertFailure(result, Result.renewFailure(expectedResult));
    }

    @ParameterizedTest
    @MethodSource("com.jobby.authorization.TestStreams#blankStringList")
    public void generateMac_whenKeyIsBlank_thenReturnFailure(String invalidKey) {
        // Arrange
        this.VALID_CONFIG.setSecretKey(invalidKey);

        Result<Void, Error> expectedResult = ValidationChain.create()
                .validateInternalNotBlank(invalidKey, "key-base-64").build();

        Mockito.when(this.safeResultValidator.validate(VALID_CONFIG))
                .thenReturn(Result.success(null));

        // Act
        var result = this.service.generateMac(VALID_DATA, VALID_CONFIG);

        // Assert
        TestAssertions.assertFailure(result, Result.renewFailure(expectedResult));
    }

    @ParameterizedTest
    @MethodSource("com.jobby.authorization.TestStreams#nonValidBase64stringList")
    public void generateMac_whenKeyIsInvalidInBase64_thenReturnFailure(String invalidKey) {
        // Arrange
        this.VALID_CONFIG.setSecretKey(invalidKey);

        Result<Void, Error> expectedResult = Result.failure(ErrorType.ITS_SERIALIZATION_ERROR,
                new Field("key-base-64", "is invalid in base64"));

        Mockito.when(this.safeResultValidator.validate(VALID_CONFIG))
                .thenReturn(Result.success(null));

        // Act
        var result = this.service.generateMac(VALID_DATA, VALID_CONFIG);

        // Assert
        TestAssertions.assertFailure(result, Result.renewFailure(expectedResult));
    }

    @ParameterizedTest
    @ValueSource(strings = {"HmacSHA2", "Unexist", "HmacNotSupported","AESNotValid"})
    public void generateMac_whenAlgorithmIsInvalid_thenReturnFailure(String invalidAlgorithm) {
        // Arrange
        this.VALID_CONFIG.setAlgorithm(invalidAlgorithm);

        var expectedResult = Result.failure(ErrorType.ITS_INVALID_OPTION_PARAMETER,
                new Field("algorithm", "The value is not within valid parameters"));

        Mockito.when(this.safeResultValidator.validate(VALID_CONFIG))
                .thenReturn(Result.success(null));

        // Act
        var result = this.service.generateMac(VALID_DATA, VALID_CONFIG);

        // Assert
        TestAssertions.assertFailure(result, Result.renewFailure(expectedResult));
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2,15,17, 31, 33, 63, 65, 127, 129})
    public void generateMac_whenKeyLengthIsInvalid_thenReturnFailure(int invalidKeyLength) {
        // Arrange
        var bytes = new byte[invalidKeyLength];
        new Random().nextBytes(bytes);
        var invalidKey = Base64.getEncoder().encodeToString(bytes);

        this.VALID_CONFIG.setSecretKey(invalidKey);

        var expectedResult = Result.failure(ErrorType.ITS_INVALID_OPTION_PARAMETER,
                new Field("key-base-64-bits", "The value is not within valid parameters"));

        Mockito.when(this.safeResultValidator.validate(VALID_CONFIG))
                .thenReturn(Result.success(null));

        // Act
        var result = this.service.generateMac(VALID_DATA, VALID_CONFIG);

        // Assert
        TestAssertions.assertFailure(result, Result.renewFailure(expectedResult));
    }

    @Test
    public void generateMac_whenDataIsNull_thenReturnFailure() {
        // Arrange
        Result<Void, Error> expectedResult = ValidationChain.create()
                .validateInternalNotBlank(null, "data").build();

        Mockito.when(this.safeResultValidator.validate(VALID_CONFIG))
                .thenReturn(Result.success(null));

        // Act
        var result = this.service.generateMac(null, VALID_CONFIG);

        // Assert
        TestAssertions.assertFailure(result, Result.renewFailure(expectedResult));
    }

    @ParameterizedTest
    @MethodSource("com.jobby.authorization.TestStreams#blankStringList")
    public void generateMac_whenDataIsBlank_thenReturnFailure(String invalidData) {
        // Arrange
        Result<Void, Error> expectedResult = ValidationChain.create()
                .validateInternalNotBlank(invalidData, "data").build();

        Mockito.when(this.safeResultValidator.validate(VALID_CONFIG))
                .thenReturn(Result.success(null));

        // Act
        var result = this.service.generateMac(invalidData, VALID_CONFIG);

        // Assert
        TestAssertions.assertFailure(result, Result.renewFailure(expectedResult));
    }

    @Test
    public void generateMac_whenBuildingIsFailed_thenReturnFailure() {
        // Arrange
        var expectedResult = Result.failure(ErrorType.ITS_OPERATION_ERROR,
                new Field("expected instance", "expected reason"));

        Mockito.when(this.safeResultValidator.validate(VALID_CONFIG))
                .thenReturn(Result.success(null));

        Mockito.when(this.defaultMacBuilder.setAlgorithm(Mockito.any())).thenReturn(this.defaultMacBuilder);
        Mockito.when(this.defaultMacBuilder.setData(Mockito.any())).thenReturn(this.defaultMacBuilder);
        Mockito.when(this.defaultMacBuilder.setKey(Mockito.any())).thenReturn(this.defaultMacBuilder);
        Mockito.when(this.defaultMacBuilder.build()).thenReturn(Result.renewFailure(expectedResult));

        // Act
        var result = this.service.generateMac(VALID_DATA, VALID_CONFIG);

        // Assert
        TestAssertions.assertFailure(result, Result.renewFailure(expectedResult));
    }

}
