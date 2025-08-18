package com.jobby.authorization.infraestructure.adapters.out;

import com.jobby.authorization.domain.ports.out.SafeResultValidator;
import com.jobby.domain.mobility.Error;
import com.jobby.domain.mobility.ErrorType;
import com.jobby.domain.mobility.Field;
import com.jobby.domain.result.Result;
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
