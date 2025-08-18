package com.jobby.authorization;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.error.ErrorType;
import com.jobby.domain.mobility.error.Field;
import com.jobby.domain.mobility.result.Result;
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
