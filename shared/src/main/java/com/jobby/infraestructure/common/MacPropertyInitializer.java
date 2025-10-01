package com.jobby.infraestructure.common;

import com.jobby.domain.configurations.MacConfig;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.error.ErrorType;
import com.jobby.domain.mobility.result.Result;
import com.jobby.domain.mobility.validator.ValidationChain;
import com.jobby.domain.ports.SafeResultValidator;
import com.jobby.domain.ports.hashing.mac.MacService;
import java.lang.reflect.Field;

public class MacPropertyInitializer {

    private final MacService macService;
    private final MacConfig macConfig;
    private boolean validConfig = false;

    public MacPropertyInitializer(MacService macService, MacConfig macConfig, SafeResultValidator safeResultValidator) {
        this.macService = macService;
        this.macConfig = macConfig;

        safeResultValidator.validate(macConfig)
                .fold(
                        onSuccess -> this.validConfig = true,
                        onFailure -> this.validConfig = false
                );
    }

    public Result<Void, Error> execute(Object entity) {
        if (!validConfig) {
            return Result.failure(ErrorType.VALIDATION_ERROR, new com.jobby.domain.mobility.error.Field("MacPropertyInitializer", "Invalid MAC configuration"));
        }

        return ValidationChain.create()
                .validateNotNull(entity, "Entity cannot be null")
                .build()
                .flatMap(v -> process(entity));
    }


    private Result<Void, Error> process(Object entity) {
        Class<?> clazz = entity.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            MacGeneratedProperty ann = field.getAnnotation(MacGeneratedProperty.class);

            if (ann != null) {
                try {
                    String sourceProp = ann.name();
                    Field sourceField = clazz.getDeclaredField(sourceProp);

                    sourceField.setAccessible(true);
                    Object sourceValue = sourceField.get(entity);

                    if (sourceValue != null) {
                        this.macService.generateMac(sourceValue.toString(), this.macConfig)
                                .flatMap(mac -> {
                                    field.setAccessible(true);
                                    try {
                                        field.set(entity, mac);
                                    } catch (IllegalAccessException e) {
                                        throw new RuntimeException(e);
                                    }
                                    return Result.success(null);
                                });

                    }
                } catch (Exception e) {
                    return Result.failure(ErrorType.VALIDATION_ERROR, new com.jobby.domain.mobility.error.Field("MacPropertyInitializer", e.getMessage()));
                }
            }
        }

        return Result.success(null);
    }
}
