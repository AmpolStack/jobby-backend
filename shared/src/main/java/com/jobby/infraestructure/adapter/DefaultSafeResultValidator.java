package com.jobby.authorization.infraestructure.adapters.out;

import com.jobby.domain.ports.SafeResultValidator;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.error.ErrorType;
import com.jobby.domain.mobility.error.Field;
import com.jobby.domain.mobility.result.Result;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DefaultSafeResultValidator implements SafeResultValidator {

    private final Validator validator;

    public DefaultSafeResultValidator(Validator validator) {
        this.validator = validator;
    }

    @Override
    public <T> Result<Void, Error> validate(T entity) {
        Set<ConstraintViolation<T>> violations = validator.validate(entity);

        if (!violations.isEmpty()) {
            Field[] fields = violations.stream()
                    .map(v -> new Field(v.getPropertyPath().toString(), v.getMessage()))
                    .toArray(Field[]::new);
            return Result.failure(ErrorType.VALIDATION_ERROR, fields);
        }

        return Result.success(null);
    }

}
