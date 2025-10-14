package com.jobby.business.infrastructure.persistence.jpa.security;

import com.jobby.business.infrastructure.persistence.jpa.entities.JpaBusinessEntity;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import com.jobby.infraestructure.common.security.encryption.DecryptionPropertyInitializer;
import com.jobby.infraestructure.common.security.encryption.EncryptionPropertyInitializer;
import com.jobby.infraestructure.common.security.mac.MacPropertyInitializer;
import com.jobby.infraestructure.security.SecurityStrategyComposer;
import org.springframework.stereotype.Component;

@Component
public class JpaBusinessEntitySecurityStrategy implements
        SecurityStrategyComposer<JpaBusinessEntity>
{
    private final EncryptionPropertyInitializer encryptionPropertyInitializer;
    private final MacPropertyInitializer macPropertyInitializer;
    private final DecryptionPropertyInitializer decryptionPropertyInitializer;

    public JpaBusinessEntitySecurityStrategy(EncryptionPropertyInitializer encryptionPropertyInitializer, MacPropertyInitializer macPropertyInitializer, DecryptionPropertyInitializer decryptionPropertyInitializer) {
        this.encryptionPropertyInitializer = encryptionPropertyInitializer;
        this.macPropertyInitializer = macPropertyInitializer;
        this.decryptionPropertyInitializer = decryptionPropertyInitializer;
    }

    @Override
    public Result<Void, Error> apply(JpaBusinessEntity jpaBusinessEntity) {
        return this.macPropertyInitializer
                .addElement(jpaBusinessEntity.getAddress())
                .processAll()
                .flatMap(v -> this.encryptionPropertyInitializer
                        .addElement(jpaBusinessEntity.getAddress())
                        .processAll());
    }

    @Override
    public Result<Void, Error> revert(JpaBusinessEntity jpaBusinessEntity) {
        return this.decryptionPropertyInitializer
                .addElement(jpaBusinessEntity)
                .processAll();
    }
}
