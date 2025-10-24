package com.jobby.business.infrastructure.persistence.business.mongo.security;

import com.jobby.business.infrastructure.persistence.business.mongo.entities.MongoBusinessEntity;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import com.jobby.infraestructure.enrichment.encryption.DecryptionPropertyInitializer;
import com.jobby.infraestructure.enrichment.mac.MacPropertyInitializer;
import com.jobby.infraestructure.security.SecurityStrategyComposer;
import org.springframework.stereotype.Component;

@Component
public class MongoBusinessEntitySecurityStrategy implements
        SecurityStrategyComposer<MongoBusinessEntity>
{
    private final DecryptionPropertyInitializer decryptionPropertyInitializer;
    private final MacPropertyInitializer macPropertyInitializer;

    public MongoBusinessEntitySecurityStrategy(DecryptionPropertyInitializer decryptionPropertyInitializer,
                                               MacPropertyInitializer macPropertyInitializer) {
        this.decryptionPropertyInitializer = decryptionPropertyInitializer;
        this.macPropertyInitializer = macPropertyInitializer;
    }

    @Override
    public Result<Void, Error> apply(MongoBusinessEntity mongoBusinessEntity) {
        return this.macPropertyInitializer
                .addElement(mongoBusinessEntity.getAddress())
                .processAll();
    }

    @Override
    public Result<Void, Error> revert(MongoBusinessEntity mongoBusinessEntity) {
        return this.decryptionPropertyInitializer
                .addElement(mongoBusinessEntity.getAddress())
                .processAll();
    }
}
