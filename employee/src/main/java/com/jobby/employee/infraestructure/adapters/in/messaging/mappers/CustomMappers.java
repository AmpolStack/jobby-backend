package com.jobby.employee.infraestructure.adapters.in.messaging.mappers;

import org.mapstruct.MapperConfig;
import org.springframework.stereotype.Component;

@Component
public class CustomMappers {
    public String fromCharSequence(CharSequence value) {
        return value == null ? null : value.toString();
    }


    public CharSequence toCharSequence(String value) {
        return value;
    }
}
