package com.jobby.authorization.domain.shared.validators;

import com.jobby.authorization.domain.shared.result.Error;
import com.jobby.authorization.domain.shared.result.ErrorType;
import com.jobby.authorization.domain.shared.result.Field;
import com.jobby.authorization.domain.shared.result.Result;
import java.util.Arrays;

public class ObjectValidator {

    public static Result<Void, Error> validateNotNullObject(Object entity, String fieldName) {
        if(entity == null) {
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field(fieldName, fieldName + " is null"));
        }
        return Result.success(null);
    }

     public static <T> Result<Void, Error> validateAnyMatch(T entity, T[] compare, String fieldName){
         if(Arrays.stream(compare).noneMatch(length -> length.equals(entity))){
             return Result.failure(ErrorType.ITN_INVALID_OPTION_PARAMETER,
                     new Field(fieldName, "The value is not within valid parameters"));
         }
         return Result.success(null);
     }

}
