package com.jobby.infraestructure.common.security.hashing;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import com.jobby.domain.ports.hashing.HashingService;
import com.jobby.infraestructure.common.security.EntityEnrichmentProcessor;

public class HashedPropertyInitializer extends EntityEnrichmentProcessor<Hashed> {

    private final HashingService hashingService;

    HashedPropertyInitializer(HashingService hashingService) {
        super(Hashed.class);
        this.hashingService = hashingService;
    }

    @Override
    protected Result<?, Error> enrichmentOperation(String inputExtracted) {
        return this.hashingService.hash(inputExtracted);
    }

}
