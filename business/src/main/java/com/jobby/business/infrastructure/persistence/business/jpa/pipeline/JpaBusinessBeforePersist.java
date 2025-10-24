package com.jobby.business.infrastructure.persistence.business.jpa.pipeline;

import com.jobby.business.domain.entities.Business;
import com.jobby.business.infrastructure.persistence.business.jpa.entities.JpaBusinessEntity;
import com.jobby.business.infrastructure.persistence.business.jpa.mappers.JpaBusinessMapper;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import com.jobby.infraestructure.repository.pipeline.BeforePersistProcess;
import com.jobby.infraestructure.security.SecurityStrategyImplementer;
import org.springframework.stereotype.Component;

@Component
public class JpaBusinessBeforePersist implements BeforePersistProcess<JpaBusinessEntity, Business> {

    private final JpaBusinessMapper jpaBusinessMapper;
    private final SecurityStrategyImplementer<JpaBusinessEntity> securityStrategyImplementer;

    public JpaBusinessBeforePersist(JpaBusinessMapper jpaBusinessMapper, SecurityStrategyImplementer<JpaBusinessEntity> securityStrategyImplementer) {
        this.jpaBusinessMapper = jpaBusinessMapper;
        this.securityStrategyImplementer = securityStrategyImplementer;
    }

    @Override
    public JpaBusinessEntity map(Business business) {
        return this.jpaBusinessMapper.toJpa(business);
    }

    @Override
    public Result<Void, Error> mutate(JpaBusinessEntity jpaBusiness) {
        return this.securityStrategyImplementer.apply(jpaBusiness);
    }
}
