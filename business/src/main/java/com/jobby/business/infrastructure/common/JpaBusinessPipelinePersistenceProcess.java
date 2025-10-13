package com.jobby.business.infrastructure.common;

import com.jobby.business.domain.entities.Business;
import com.jobby.business.infrastructure.common.repository.pipeline.PipelinePersistenceProcess;
import com.jobby.business.infrastructure.persistence.jpa.entities.JpaBusinessEntity;
import com.jobby.business.infrastructure.persistence.jpa.mappers.JpaBusinessMapper;
import com.jobby.business.infrastructure.secure.SecurityStrategyImplementer;
import com.jobby.business.infrastructure.secure.SecurityStrategyReverter;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.error.ErrorType;
import com.jobby.domain.mobility.error.Field;
import com.jobby.domain.mobility.result.Result;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JpaBusinessPipelinePersistenceProcess implements
        PipelinePersistenceProcess<JpaBusinessEntity, Business>
{
    private final JpaBusinessMapper jpaBusinessMapper;
    private final SecurityStrategyReverter<JpaBusinessEntity> securityStrategyReverter;
    private final SecurityStrategyImplementer<JpaBusinessEntity> securityStrategyImplementer;

    public JpaBusinessPipelinePersistenceProcess(JpaBusinessMapper jpaBusinessMapper, SecurityStrategyReverter<JpaBusinessEntity> securityStrategyReverter, SecurityStrategyImplementer<JpaBusinessEntity> securityStrategyImplementer) {
        this.jpaBusinessMapper = jpaBusinessMapper;
        this.securityStrategyReverter = securityStrategyReverter;
        this.securityStrategyImplementer = securityStrategyImplementer;
    }

    @Override
    public Result<Business, Error> after(Optional<JpaBusinessEntity> jpaBusinessOptional) {
        if(jpaBusinessOptional.isEmpty()){
            return Result.failure(ErrorType.USER_NOT_FOUND,
                    new Field("business", "entity not foud"));
        }

        var jpaBusiness = jpaBusinessOptional.get();
        return this.securityStrategyReverter.revert(jpaBusiness)
                .map(v -> this.jpaBusinessMapper.toDomain(jpaBusiness));
    }

    @Override
    public Result<JpaBusinessEntity, Error> before(Business business) {
        var mapped = this.jpaBusinessMapper.toJpa(business);
        return this.securityStrategyImplementer.apply(mapped)
                .map(v -> mapped);
    }
}
