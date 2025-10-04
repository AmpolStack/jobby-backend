package com.jobby.infraestructure.common;

import com.jobby.domain.ports.hashing.HashingService;

public class HashedPropertyInitializer extends ReflexiveSetterProcessor<HashedProperty, Void> {
    HashedPropertyInitializer(HashingService hashingService) {
        super(hashingService::hash,HashedProperty.class);
    }
}
