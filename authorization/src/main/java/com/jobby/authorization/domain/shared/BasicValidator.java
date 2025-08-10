package com.jobby.authorization.domain.shared;

import com.jobby.authorization.domain.result.Error;
import com.jobby.authorization.domain.result.ErrorType;
import com.jobby.authorization.domain.result.Field;
import com.jobby.authorization.domain.result.Result;

public class BasicValidator {
    
    public static Result<Void, Error> validateNotNullObject(Object entity, String fieldName) {
        if(entity == null) {
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field(fieldName, fieldName + " is null"));
        }
        return Result.success(null);
    }
    
    public static Result<Void, Error> validateNotBlankString(String entity, String fieldName) {
        return validateNotNullObject(entity, fieldName)
                .flatMap(x -> {
                    if(entity.isBlank()){
                        return Result.failure(ErrorType.VALIDATION_ERROR,
                                new Field(fieldName, fieldName + " is blank"));
                    }
                    return Result.success(null);
                });
    }
    
    public static Result<Void, Error> validateGreaterInteger(int source, int compared, String fieldName) {
        return validateGreaterLong(source, compared, fieldName);
    }
    
    public static Result<Void, Error> validateGreaterLong(long source, long compared, String fieldName) {
        if(source < compared){
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field(fieldName, fieldName + " is less than " + compared));
        }
        return Result.success(null);
    }
}
