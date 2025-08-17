package com.jobby.authorization.domain.shared.validators;

import com.jobby.authorization.domain.shared.errors.Error;
import com.jobby.authorization.domain.shared.errors.ErrorType;
import com.jobby.authorization.domain.shared.errors.Field;
import com.jobby.authorization.domain.shared.result.Result;

public class StringValidator {

    public static Result<Void, Error> validateNotBlankString(String entity, String fieldName) {
        return ObjectValidator.validateNotNullObject(entity, fieldName)
                .flatMap(x -> {
                    if(entity.isBlank()){
                        return Result.failure(ErrorType.VALIDATION_ERROR,
                                new Field(fieldName, fieldName + " is blank"));
                    }
                    return Result.success(null);
                });
    }

}
