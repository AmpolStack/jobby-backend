package com.jobby.infraestructure.common;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.error.ErrorType;
import com.jobby.domain.mobility.result.Result;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.function.Function;

public class ReflexiveSetterProcessor<A extends Annotation>{

    protected Result<Void, Error> process(Object entity, Class<A> annotationClass, Function<String, Result<?, Error>> setterFunction) {
        Class<?> clazz = entity.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            A ann = field.getAnnotation(annotationClass);

            if(ann == null) {
                continue;
            }

            try {
                field.setAccessible(true);
                var sourceValue = field.get(entity);

                if(sourceValue == null) {
                    continue;
                }

                if (!(sourceValue instanceof String sourceStringValue)) {
                    return Result.failure(ErrorType.ITN_VALIDATION_TYPE, new com.jobby.domain.mobility.error.Field(field.getName(), "Source property must be a String"));
                }

                setterFunction.apply(sourceStringValue)
                        .flatMap(encryptedValue -> {
                            try {
                                field.setAccessible(true);
                                field.set(entity, encryptedValue);
                                return Result.success(null);
                            } catch (IllegalAccessException e) {
                                return Result.failure(ErrorType.ITS_OPERATION_ERROR, new com.jobby.domain.mobility.error.Field(field.getName(), e.getMessage()));
                            }
                        });

            } catch (Exception e) {
                return Result.failure(ErrorType.VALIDATION_ERROR, new com.jobby.domain.mobility.error.Field("MacPropertyInitializer", e.getMessage()));
            }

        }

        return Result.success(null);
    }
}
