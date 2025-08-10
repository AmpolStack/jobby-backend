package com.jobby.authorization.domain.shared.validators;

import com.jobby.authorization.domain.shared.result.Error;
import com.jobby.authorization.domain.shared.result.ErrorType;
import com.jobby.authorization.domain.shared.result.Field;
import com.jobby.authorization.domain.shared.result.Result;

public class NumberValidator {

    public static Result<Void, Error> validateGreaterInteger(int source, int compared, String fieldName) {
        return validateGreaterLong(source, compared, fieldName);
    }

    public static Result<Void, Error> validateSmallerInteger(int source, int compared, String fieldName) {
        return validateSmallerLong(source, compared, fieldName);
    }


    public static Result<Void, Error> validateGreaterNotEqualsInteger(int source, int compared, String fieldName) {
        return validateGreaterLong(source, compared, fieldName);
    }

    public static Result<Void, Error> validateSmallerNotEqualsInteger(int source, int compared, String fieldName) {
        return validateSmallerLong(source, compared, fieldName);
    }

    public static Result<Void, Error> validateGreaterLong(long source, long compared, String fieldName) {
        if(source < compared){
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field(fieldName, fieldName + " is less than " + compared));
        }
        return Result.success(null);
    }

    public static Result<Void, Error> validateSmallerLong(long source, long compared, String fieldName) {
        if(source > compared){
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field(fieldName, fieldName + " is bigger than " + compared));
        }
        return Result.success(null);
    }

    public static Result<Void, Error> validateGreaterNotEqualsLong(long source, long compared, String fieldName) {
        if(source <= compared){
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field(fieldName, fieldName + "equals or is less than " + compared));
        }
        return Result.success(null);
    }

    public static Result<Void, Error> validateSmallerNotEqualsLong(long source, long compared, String fieldName) {
        if(source >= compared){
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field(fieldName, fieldName + " equals or is bigger than " + compared));
        }
        return Result.success(null);
    }
}
