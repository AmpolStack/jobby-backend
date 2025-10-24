package com.jobby.business.infrastructure.persistence.business.jpa.pipeline;

import com.jobby.business.domain.entities.Business;
import com.jobby.business.infrastructure.persistence.business.jpa.entities.JpaBusinessEntity;
import com.jobby.business.infrastructure.persistence.business.jpa.mappers.JpaBusinessMapper;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import com.jobby.infraestructure.repository.pipeline.AfterPersistProcess;
import com.jobby.infraestructure.security.SecurityStrategyReverter;
import org.springframework.stereotype.Component;

@Component
public class JpaBusinessAfterPersist implements AfterPersistProcess<JpaBusinessEntity, Business> {

    private final JpaBusinessMapper jpaBusinessMapper;
    private final SecurityStrategyReverter<JpaBusinessEntity> securityStrategyReverter;

    public JpaBusinessAfterPersist(JpaBusinessMapper jpaBusinessMapper, SecurityStrategyReverter<JpaBusinessEntity> securityStrategyReverter) {
        this.jpaBusinessMapper = jpaBusinessMapper;
        this.securityStrategyReverter = securityStrategyReverter;
    }

    @Override
    public Business map(JpaBusinessEntity jpaBusiness) {
        return this.jpaBusinessMapper.toDomain(jpaBusiness);
    }

    @Override
    public String getEntityName() {
        return "business";
    }

    @Override
    public Result<Void, Error> mutate(JpaBusinessEntity jpaBusiness) {
        return this.securityStrategyReverter.revert(jpaBusiness);
    }
}
