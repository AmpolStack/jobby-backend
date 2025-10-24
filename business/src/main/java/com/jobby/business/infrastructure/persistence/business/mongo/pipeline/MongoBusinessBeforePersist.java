package com.jobby.business.infrastructure.persistence.business.mongo.pipeline;

import com.jobby.business.domain.entities.Business;
import com.jobby.business.infrastructure.persistence.business.mongo.entities.MongoBusinessEntity;
import com.jobby.business.infrastructure.persistence.business.mongo.mappers.MongoBusinessMapper;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import com.jobby.infraestructure.repository.pipeline.BeforePersistProcess;
import com.jobby.infraestructure.security.SecurityStrategyImplementer;
import org.springframework.stereotype.Component;

@Component
public class MongoBusinessBeforePersist implements BeforePersistProcess<MongoBusinessEntity, Business> {

    private final MongoBusinessMapper mongoBusinessMapper;
    private final SecurityStrategyImplementer<MongoBusinessEntity> securityStrategyImplementer;

    public MongoBusinessBeforePersist(MongoBusinessMapper mongoBusinessMapper, SecurityStrategyImplementer<MongoBusinessEntity> securityStrategyImplementer) {
        this.mongoBusinessMapper = mongoBusinessMapper;
        this.securityStrategyImplementer = securityStrategyImplementer;
    }


    @Override
    public MongoBusinessEntity map(Business business) {
        return this.mongoBusinessMapper.toDocument(business);
    }

    @Override
    public Result<Void, Error> mutate(MongoBusinessEntity mongoBusinessEntity) {
        return this.securityStrategyImplementer.apply(mongoBusinessEntity);
    }
}
