package com.jobby.infraestructure.common.security;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.error.ErrorType;
import com.jobby.domain.mobility.result.Result;
import com.jobby.domain.ports.SafeResultValidator;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ReflectivePropertyAssigner<A extends Annotation, C >{

    private boolean validConfig = false;
    private final Function<String, Result<?, Error>> setterFunction;
    private final Class<A> annotationClass;
    private com.jobby.domain.mobility.error.Field errorField = null;
    private BiFunction<A, Class<?>, Field> customSourceFieldExtractor = null;

    private final List<Object> elements = new ArrayList<>();

    public ReflectivePropertyAssigner(C config, SafeResultValidator safeResultValidator, Function<String, Result<?, Error>> setterFunction, com.jobby.domain.mobility.error.Field errorField, Class<A> annotationClass) {
        this.setterFunction = setterFunction;
        this.annotationClass = annotationClass;
        this.errorField = errorField;

        safeResultValidator.validate(config)
                .fold(
                        onSuccess -> this.validConfig = true,
                        onFailure -> this.validConfig = false
                );
    }

    public ReflectivePropertyAssigner(Function<String, Result<?, Error>> setterFunction, Class<A> annotationClass) {
        this.setterFunction = setterFunction;
        this.annotationClass = annotationClass;

        this.validConfig = true;
    }

    public ReflectivePropertyAssigner<A,C> addElement(Object element) {
        this.elements.add(element);
        return this;
    }

    public Result<Void, Error> processAll() {
        if (!this.validConfig) {
            return Result.failure(ErrorType.VALIDATION_ERROR, this.errorField);
        }

        for (Object element : this.elements) {
            var result = apply(element, annotationClass);
            if (result.isFailure()) {
                return result;
            }
        }

        return Result.success(null);
    }

    private Result<Void, Error> apply(Object entity, Class<A> annotationClass)
    {
        Class<?> clazz = entity.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            A ann = field.getAnnotation(annotationClass);

            if(ann == null) {
                continue;
            }

            try {
                Field fieldTarget;

                if(this.customSourceFieldExtractor != null) {
                    fieldTarget = this.customSourceFieldExtractor.apply(ann, clazz);
                }
                else{
                    fieldTarget = field;
                }

                fieldTarget.setAccessible(true);
                var sourceValue = fieldTarget.get(entity);


                if(sourceValue == null) {
                    continue;
                }

                if (!(sourceValue instanceof String sourceStringValue)) {
                    return Result.failure(ErrorType.ITN_VALIDATION_TYPE, new com.jobby.domain.mobility.error.Field(field.getName(), "Source property must be a String"));
                }

                this.setterFunction.apply(sourceStringValue)
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

    protected void setCustomSourceFieldExtractor(BiFunction<A, Class<?>, Field> extractor) {
        this.customSourceFieldExtractor = extractor;
    }
}
