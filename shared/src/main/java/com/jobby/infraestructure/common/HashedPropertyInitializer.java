package com.jobby.infraestructure.common;

import com.jobby.domain.ports.hashing.HashingService;
import com.jobby.infraestructure.common.security.ReflexiveSetterProcessor;

public class HashedPropertyInitializer extends ReflexiveSetterProcessor<HashedProperty, Void> {
    HashedPropertyInitializer(HashingService hashingService) {
        super(hashingService::hash,HashedProperty.class);
    }
}
