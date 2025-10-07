package com.jobby.infraestructure.common.security.mac;

import com.jobby.domain.configurations.MacConfig;
import com.jobby.domain.mobility.error.Field;
import com.jobby.domain.ports.SafeResultValidator;
import com.jobby.domain.ports.hashing.mac.MacService;
import com.jobby.infraestructure.common.security.EntityEnrichmentProcessor;

public class MacPropertyInitializer extends EntityEnrichmentProcessor<MacGenerated, MacConfig> {

    public MacPropertyInitializer(MacService macService, MacConfig macConfig, SafeResultValidator safeResultValidator) {
        super(macConfig, safeResultValidator,
                (sourceValue) -> macService.generateMac(sourceValue, macConfig),
                new Field("MacPropertyInitializer", "Invalid MAC configuration"),
                MacGenerated.class);

        this.setCustomSourceFieldExtractor((ann, clazz) ->{
            String sourceProp = ann.name();
            try {
                return clazz.getDeclaredField(sourceProp);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
