package com.jobby.infraestructure.enrichment;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.error.ErrorType;
import com.jobby.domain.mobility.result.Result;
import com.jobby.domain.ports.SafeResultValidator;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class EntityEnrichmentProcessor<A extends Annotation>
    implements SourceFieldExtractor<A>{

    private boolean validConfig = false;
    private final Class<A> annotationClass;
    private com.jobby.domain.mobility.error.Error validationError;
    private final List<Object> elements = new ArrayList<>();

    protected abstract Result<?, Error> enrichmentOperation(String inputExtracted);

    public EntityEnrichmentProcessor(Class<A> annotationClass) {
        this.annotationClass = annotationClass;
        this.validConfig = true;
    }

    public EntityEnrichmentProcessor(Object config, Class<A> annotationClass, SafeResultValidator safeResultValidator) {
        this.annotationClass = annotationClass;
        safeResultValidator.validate(config)
                .fold((onSuccess) -> this.validConfig = true,
                        (onFailure) -> {
                    this.validConfig = false;
                    this.validationError = onFailure;
                });
    }

    public EntityEnrichmentProcessor<A> addElement(Object element) {
        this.elements.add(element);
        return this;
    }

    public Result<Void, Error> processAll() {
        if (!this.validConfig) {
            return Result.failure(this.validationError);
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

                var customExtractor = this.customExtraction(clazz, ann);
                fieldTarget = Objects.requireNonNullElse(customExtractor, field);

                fieldTarget.setAccessible(true);
                var sourceValue = fieldTarget.get(entity);


                if(sourceValue == null) {
                    continue;
                }

                if (!(sourceValue instanceof String sourceStringValue)) {
                    return Result.failure(ErrorType.ITN_VALIDATION_TYPE, new com.jobby.domain.mobility.error.Field(field.getName(), "Source property must be a String"));
                }

                this.enrichmentOperation(sourceStringValue)
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
