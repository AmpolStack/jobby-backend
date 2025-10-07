package com.jobby.infraestructure.common.security.hashing;

import com.jobby.domain.ports.hashing.HashingService;
import com.jobby.infraestructure.common.security.EntityEnrichmentProcessor;

public class HashedPropertyInitializer extends EntityEnrichmentProcessor<Hashed, Void> {
    HashedPropertyInitializer(HashingService hashingService) {
        super(hashingService::hash, Hashed.class);
    }
}
