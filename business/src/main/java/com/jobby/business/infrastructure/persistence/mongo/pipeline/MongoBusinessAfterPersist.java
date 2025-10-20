package com.jobby.business.infrastructure.persistence.mongo.pipeline;

import com.jobby.business.domain.entities.Business;
import com.jobby.business.infrastructure.persistence.mongo.entities.MongoBusinessEntity;
import com.jobby.business.infrastructure.persistence.mongo.mappers.MongoBusinessMapper;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import com.jobby.infraestructure.repository.pipeline.AfterPersistProcess;
import com.jobby.infraestructure.security.SecurityStrategyReverter;
import org.springframework.stereotype.Component;

@Component
public class MongoBusinessAfterPersist implements AfterPersistProcess<MongoBusinessEntity, Business> {

    private final MongoBusinessMapper mongoBusinessMapper;
    private final SecurityStrategyReverter<MongoBusinessEntity> securityStrategyReverter;

    public MongoBusinessAfterPersist(MongoBusinessMapper mongoBusinessMapper, SecurityStrategyReverter<MongoBusinessEntity> securityStrategyReverter) {
        this.mongoBusinessMapper = mongoBusinessMapper;
        this.securityStrategyReverter = securityStrategyReverter;
    }


    @Override
    public Business map(MongoBusinessEntity mongoBusinessEntity) {
        return this.mongoBusinessMapper.toDomain(mongoBusinessEntity);
    }

    @Override
    public String getEntityName() {
        return "business";
    }

    @Override
    public Result<Void, Error> mutate(MongoBusinessEntity mongoBusinessEntity) {
        return this.securityStrategyReverter.revert(mongoBusinessEntity);
    }
}
