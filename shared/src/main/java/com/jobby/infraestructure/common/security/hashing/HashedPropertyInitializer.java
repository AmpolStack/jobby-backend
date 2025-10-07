package com.jobby.infraestructure.common.security.hashing;

import com.jobby.domain.ports.hashing.HashingService;
import com.jobby.infraestructure.common.security.ReflectivePropertyAssigner;

public class HashedPropertyInitializer extends ReflectivePropertyAssigner<Hashed, Void> {
    HashedPropertyInitializer(HashingService hashingService) {
        super(hashingService::hash, Hashed.class);
    }
}
