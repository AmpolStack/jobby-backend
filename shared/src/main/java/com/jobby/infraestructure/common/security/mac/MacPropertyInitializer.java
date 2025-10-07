package com.jobby.infraestructure.common.security.mac;

import com.jobby.domain.configurations.MacConfig;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import com.jobby.domain.ports.SafeResultValidator;
import com.jobby.domain.ports.hashing.mac.MacService;
import com.jobby.infraestructure.common.security.EntityEnrichmentProcessor;

import java.lang.reflect.Field;

public class MacPropertyInitializer extends EntityEnrichmentProcessor<MacGenerated> {

    private final MacService macService;
    private final MacConfig macConfig;

    public MacPropertyInitializer(MacService macService,
                                  MacConfig macConfig,
                                  SafeResultValidator safeResultValidator) {
        super(macConfig, MacGenerated.class, safeResultValidator);

        this.macService = macService;
        this.macConfig = macConfig;
    }

    @Override
    public Field customExtraction(Class<?> clazz, MacGenerated ann) {
        String sourceProp = ann.name();
        try {
            return clazz.getDeclaredField(sourceProp);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Result<?, Error> enrichmentOperation(String inputExtracted) {
        return this.macService.generateMac(inputExtracted, this.macConfig);
    }

}
