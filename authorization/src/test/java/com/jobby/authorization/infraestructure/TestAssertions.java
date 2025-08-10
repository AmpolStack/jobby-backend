package com.jobby.authorization.infraestructure;

import com.jobby.authorization.domain.shared.result.Error;
import com.jobby.authorization.domain.shared.result.ErrorType;
import com.jobby.authorization.domain.shared.result.Field;
import com.jobby.authorization.domain.shared.result.Result;

import static org.junit.jupiter.api.Assertions.*;

public final class TestAssertions {

    private TestAssertions() {}

    public static <T> void assertFailure(Result<T, Error> response,
                                         ErrorType errorType,
                                         String fieldName,
                                         String message) {
        var expected = Result.failure(errorType, new Field(fieldName, message));
        assertEquals(expected, response);
        assertTrue(response.isFailure());
    }

    public static <T> void assertSuccess(Result<T, Error> response) {
        assertTrue(response.isSuccess());
    }
}
