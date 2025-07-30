package com.jobby.authorization.infraestructure.adapters.out.encrypt;

import com.jobby.authorization.domain.result.ErrorType;
import com.jobby.authorization.domain.result.Field;
import com.jobby.authorization.domain.result.Result;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AESEncryptionServiceTest {

    @Mock
    private DefaultEncryptBuilder defaultEncryptBuilder;

    @InjectMocks
    private AESEncryptionService aesEncryptionService;

    @ParameterizedTest
    @ValueSource(ints = { -2, -200, 0, 17, 200})
    public void encrypt_whenIvLengthAreInvalid(int ivLength) {
        // Arrange
        var data = "example_data";
        var key = "p652zw20jx/Bvg/4I7Mrdg==";

        var expectedResult = Result.failure(ErrorType.ITN_INVALID_OPTION_PARAMETER,
                new Field(
                        "ivLength",
                        "IV length must be between 1 and 16 bytes"
                )
        );
        // Act
        var result = this.aesEncryptionService.encrypt(data, key, ivLength);

        // Assert
        Assertions.assertTrue(result.isFailure());
        Assertions.assertEquals(expectedResult, result);
    }



}
