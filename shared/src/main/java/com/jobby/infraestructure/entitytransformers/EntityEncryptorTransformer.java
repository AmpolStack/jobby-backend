package com.jobby.infraestructure.entitytransformers;

import com.jobby.domain.configurations.EncryptConfig;
import com.jobby.domain.ports.encrypt.EncryptionService;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Converter
public class EntityEncryptorTransformer implements AttributeConverter<String,String> {

    private final EncryptionService encryptionService;
    private final EncryptConfig encryptConfig;

    public EntityEncryptorTransformer(EncryptionService encryptionService, EncryptConfig encryptConfig) {
        this.encryptionService = encryptionService;
        this.encryptConfig = encryptConfig;
    }

    @Override
    public String convertToDatabaseColumn(String s) {
        var converted = this.encryptionService.encrypt(s, this.encryptConfig);
        if(converted.isFailure()){
            log.error(converted.getError().toString());
        }
        return converted.getData();
    }

    @Override
    public String convertToEntityAttribute(String s) {
        return s;
    }
}
