package com.jobby.business.infrastructure.secure;

import com.jobby.business.infrastructure.persistence.mongo.entities.MongoBusinessEntity;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import com.jobby.infraestructure.common.security.encryption.EncryptionPropertyInitializer;
import com.jobby.infraestructure.common.security.mac.MacPropertyInitializer;
import org.springframework.stereotype.Component;

@Component
public class MongoBusinessEntitySecurityStrategy implements
        SecurityStrategyComposer<MongoBusinessEntity>
{
    private final EncryptionPropertyInitializer encryptionPropertyInitializer;
    private final MacPropertyInitializer macPropertyInitializer;

    public MongoBusinessEntitySecurityStrategy(EncryptionPropertyInitializer encryptionPropertyInitializer,
                                               MacPropertyInitializer macPropertyInitializer) {
        this.encryptionPropertyInitializer = encryptionPropertyInitializer;
        this.macPropertyInitializer = macPropertyInitializer;
    }

    @Override
    public Result<Void, Error> apply(MongoBusinessEntity mongoBusinessEntity) {
        return this.macPropertyInitializer
                .addElement(mongoBusinessEntity)
                .processAll();
    }

    @Override
    public Result<Void, Error> revert(MongoBusinessEntity mongoBusinessEntity) {
        return this.encryptionPropertyInitializer
                .addElement(mongoBusinessEntity)
                .processAll();
    }
}
