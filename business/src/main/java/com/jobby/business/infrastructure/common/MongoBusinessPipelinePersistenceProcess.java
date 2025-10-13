package com.jobby.business.infrastructure.common;

import com.jobby.business.domain.entities.Business;
import com.jobby.business.infrastructure.persistence.mongo.entities.MongoBusinessEntity;
import com.jobby.business.infrastructure.persistence.mongo.mappers.MongoBusinessMapper;
import com.jobby.business.infrastructure.secure.SecurityStrategyImplementer;
import com.jobby.business.infrastructure.secure.SecurityStrategyReverter;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.error.ErrorType;
import com.jobby.domain.mobility.error.Field;
import com.jobby.domain.mobility.result.Result;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class MongoBusinessPipelinePersistProcess implements
        PersistPipelineProcess<Business, MongoBusinessEntity>
{

    private final MongoBusinessMapper mongoBusinessMapper;
    private final SecurityStrategyReverter<MongoBusinessEntity> securityStrategyReverter;
    private final SecurityStrategyImplementer<MongoBusinessEntity> securityStrategyImplementer;

    public MongoBusinessPipelinePersistProcess(MongoBusinessMapper mongoBusinessMapper,
                                               SecurityStrategyReverter<MongoBusinessEntity> securityStrategyReverter,
                                               SecurityStrategyImplementer<MongoBusinessEntity> securityStrategyImplementer) {
        this.mongoBusinessMapper = mongoBusinessMapper;
        this.securityStrategyReverter = securityStrategyReverter;
        this.securityStrategyImplementer = securityStrategyImplementer;
    }

    @Override
    public Result<Business, Error> use(Optional<MongoBusinessEntity> mongoBusinessOptional) {
        if(mongoBusinessOptional.isEmpty()){
            return Result.failure(ErrorType.USER_NOT_FOUND,
                    new Field("business", "entity not foud"));
        }

        var jpaBusiness = mongoBusinessOptional.get();
        return this.securityStrategyReverter.revert(jpaBusiness)
                .map(v -> this.mongoBusinessMapper.toDomain(jpaBusiness));
    }

    @Override
    public Result<MongoBusinessEntity, Error> use(Business infra) {
        var mapped = this.mongoBusinessMapper.toDocument(infra);
        return this.securityStrategyImplementer.apply(mapped)
                .map(v -> mapped);
    }
}
