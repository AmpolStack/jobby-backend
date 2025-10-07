package com.jobby.infraestructure.common.security;

import java.lang.reflect.Field;

public interface SourceFieldExtractor<A> {
    default Field customExtraction(Class<?> clazz, A ann){
        return null;
    }
}
