package com.jobby.infraestructure.entitytransformers;

import com.jobby.domain.ports.hashing.HashingService;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Converter
public class EntityPasswordTransformer implements AttributeConverter<String,String> {

    private final HashingService hashingService;

    public EntityPasswordTransformer(HashingService hashingService) {
        this.hashingService = hashingService;
    }


    @Override
    public String convertToDatabaseColumn(String s) {
        var converted = this.hashingService.hash(s);
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
