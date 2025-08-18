package com.jobby.authorization;

import com.jobby.authorization.domain.shared.errors.Error;
import com.jobby.authorization.domain.shared.errors.ErrorType;
import com.jobby.authorization.domain.shared.errors.Field;
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

    public static <T> void assertFailure(Result<T, Error> response,
                                         Result<T, Error> expected) {
        assertEquals(expected, response);
        assertTrue(response.isFailure());
    }


    public static <T> void assertSuccess(Result<T, Error> response) {
        assertTrue(response.isSuccess());
    }
}
