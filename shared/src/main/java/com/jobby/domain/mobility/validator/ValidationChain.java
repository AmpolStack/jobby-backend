package com.jobby.domain.mobility.validator;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.error.ErrorType;
import com.jobby.domain.mobility.error.Field;
import com.jobby.domain.mobility.result.Result;

import java.util.*;
import java.util.function.Supplier;

// short-circuit validation
public class ValidationChain {
    private final Queue<Supplier<Result<?, Error>>> validations = new LinkedList<>();

    // Add lazy validations
    public ValidationChain add(Supplier<Result<?, Error>> validation) {
        validations.add(validation);
        return this;
    }


    public ValidationChain add(Result<?, Error> validation) {
        return add(() -> validation);
    }

    // utils validations
    public ValidationChain validateNotNull(Object value, String fieldName) {
        return add(() -> {
            if (value == null) {
                return Result.failure(ErrorType.VALIDATION_ERROR,
                        new Field(fieldName, fieldName + " is null"));
            }
            return Result.success(null);
        });
    }

    public ValidationChain validateNotBlank(String value, String fieldName) {
        return add(() -> {
            if (value == null) {
                return Result.failure(ErrorType.VALIDATION_ERROR,
                        new Field(fieldName, fieldName + " is null"));
            }
            if (value.isBlank()) {
                return Result.failure(ErrorType.VALIDATION_ERROR,
                        new Field(fieldName, fieldName + " is blank"));
            }
            return Result.success(null);
        });
    }

    public ValidationChain validateEmail(String email, String fieldName) {
        return add(() -> {
            if (email == null) {
                return Result.failure(ErrorType.VALIDATION_ERROR,
                        new Field(fieldName, fieldName + " is null"));
            }
            if (email.isBlank()) {
                return Result.failure(ErrorType.VALIDATION_ERROR,
                        new Field(fieldName, fieldName + " is blank"));
            }
            String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
            if (!email.matches(emailRegex)) {
                return Result.failure(ErrorType.VALIDATION_ERROR,
                        new Field(fieldName, fieldName + " must be a valid email"));
            }
            return Result.success(null);
        });
    }

    public ValidationChain validateGreaterThan(int value, int threshold, String fieldName) {
        return add(() -> {
            if (value < threshold) {
                return Result.failure(ErrorType.VALIDATION_ERROR,
                        new Field(fieldName, fieldName + " is less than " + threshold));
            }
            return Result.success(null);
        });
    }

    public ValidationChain validateSmallerThan(int value, int threshold, String fieldName) {
        return add(() -> {
            if (value > threshold) {
                return Result.failure(ErrorType.VALIDATION_ERROR,
                        new Field(fieldName, fieldName + " is bigger than " + threshold));
            }
            return Result.success(null);
        });
    }

    public ValidationChain validateGreaterOrEqualsThan(int value, int threshold, String fieldName) {
        return add(() -> {
            if (value <= threshold) {
                return Result.failure(ErrorType.VALIDATION_ERROR,
                        new Field(fieldName, fieldName + " is less than " + threshold));
            }
            return Result.success(null);
        });
    }

    public ValidationChain validateSmallerOrEqualsThan(int value, int threshold, String fieldName) {
        return add(() -> {
            if (value >= threshold) {
                return Result.failure(ErrorType.VALIDATION_ERROR,
                        new Field(fieldName, fieldName + " is bigger than " + threshold));
            }
            return Result.success(null);
        });
    }

    // internal validations
    public ValidationChain validateInternalNotNull(Object value, String fieldName) {
        return add(() -> {
            if (value == null) {
                return Result.failure(ErrorType.ITN_VALIDATION_NULL,
                        new Field(fieldName, "Internal validation failed: " + fieldName + " is null"));
            }
            return Result.success(null);
        });
    }

    public ValidationChain validateInternalNotBlank(String value, String fieldName) {
        return add(() -> {
            if (value == null) {
                return Result.failure(ErrorType.ITN_VALIDATION_NULL,
                        new Field(fieldName, "Internal validation failed: " + fieldName + " is null"));
            }
            if (value.isBlank()) {
                return Result.failure(ErrorType.ITN_VALIDATION_BLANK,
                        new Field(fieldName, "Internal validation failed: " + fieldName + " is blank"));
            }
            return Result.success(null);
        });
    }

    public ValidationChain validateInternalEmail(String email, String fieldName) {
        return add(() -> {
            if (email == null) {
                return Result.failure(ErrorType.ITN_VALIDATION_NULL,
                        new Field(fieldName, fieldName + " is null"));
            }
            if (email.isBlank()) {
                return Result.failure(ErrorType.ITN_VALIDATION_BLANK,
                        new Field(fieldName, fieldName + " is blank"));
            }
            String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
            if (!email.matches(emailRegex)) {
                return Result.failure(ErrorType.ITN_VALIDATION_FORMAT,
                        new Field(fieldName, fieldName + " must be a valid email"));
            }
            return Result.success(null);
        });
    }

    public ValidationChain validateInternalGreaterThan(int value, int threshold, String fieldName) {
        return add(() -> {
            if (value < threshold) {
                return Result.failure(ErrorType.ITN_VALIDATION_RANGE,
                        new Field(fieldName, fieldName + " is less than " + threshold));
            }
            return Result.success(null);
        });
    }

    public ValidationChain validateInternalGreaterThan(long value, long threshold, String fieldName) {
        return add(() -> {
            if (value < threshold) {
                return Result.failure(ErrorType.ITN_VALIDATION_RANGE,
                        new Field(fieldName, fieldName + " is less than " + threshold));
            }
            return Result.success(null);
        });
    }

    public ValidationChain validateInternalSmallerThan(int value, int threshold, String fieldName) {
        return add(() -> {
            if (value > threshold) {
                return Result.failure(ErrorType.ITN_VALIDATION_RANGE,
                        new Field(fieldName, fieldName + " is bigger than " + threshold));
            }
            return Result.success(null);
        });
    }

    public ValidationChain validateInternalGreaterOrEqualsThan(int value, int threshold, String fieldName) {
        return add(() -> {
            if (value <= threshold) {
                return Result.failure(ErrorType.ITN_VALIDATION_RANGE,
                        new Field(fieldName, fieldName + " is less than " + threshold));
            }
            return Result.success(null);
        });
    }

    public ValidationChain validateInternalSmallerOrEqualsThan(int value, int threshold, String fieldName) {
        return add(() -> {
            if (value >= threshold) {
                return Result.failure(ErrorType.ITN_VALIDATION_RANGE,
                        new Field(fieldName, fieldName + " is bigger than " + threshold));
            }
            return Result.success(null);
        });
    }

    public <T> ValidationChain validateInternalAnyMatch(T value, T[] threshold, String fieldName) {
        return add(() -> {
            if(Arrays.stream(threshold).noneMatch(length -> length.equals(value))){
                return Result.failure(ErrorType.ITS_INVALID_OPTION_PARAMETER,
                        new Field(fieldName, "The value is not within valid parameters"));
            }
            return Result.success(null);
        });
    }

    // custom validations
    public ValidationChain validateCustom(boolean condition, String fieldName, String message) {
        return add(() -> {
            if (!condition) {
                return Result.failure(ErrorType.VALIDATION_ERROR,
                        new Field(fieldName, message));
            }
            return Result.success(null);
        });
    }

    public ValidationChain validateInternalCustom(boolean condition, String fieldName, String message) {
        return add(() -> {
            if (!condition) {
                return Result.failure(ErrorType.ITN_VALIDATION_CUSTOM,
                        new Field(fieldName, "Internal validation failed: " + message));
            }
            return Result.success(null);
        });
    }

    // conditional validations
    public ValidationChain validateIf(boolean condition, Supplier<Result<?, Error>> validation) {
        if (condition) {
            return add(validation);
        }
        return this;
    }

    // execute all short-circuit validations in real-time
    public Result<Void, Error> build() {
        for (Supplier<Result<?, Error>> validation : validations) {
            Result<?, Error> result = validation.get(); // it only runs here
            if (result.isFailure()) {
                return Result.failure(result.getError());
            }
        }
        return Result.success(null);
    }

    public static ValidationChain create() {
        return new ValidationChain();
    }
}