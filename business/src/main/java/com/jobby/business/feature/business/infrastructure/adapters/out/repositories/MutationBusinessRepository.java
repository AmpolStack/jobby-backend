package com.jobby.business.feature.business.infrastructure.adapters.out.repositories;

import com.jobby.business.feature.business.infrastructure.persistence.mongo.MongoBusinessEntity;
import com.jobby.business.feature.business.infrastructure.persistence.mongo.SpringDataMongoBusinessRepository;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.error.ErrorType;
import com.jobby.domain.mobility.error.Field;
import com.jobby.domain.mobility.result.Result;
import com.jobby.infraestructure.transaction.orchetration.TransactionalOrchestrator;
import org.springframework.stereotype.Component;

@Component
public class MutationBusinessRepository {

    private final TransactionalOrchestrator transaction;
    private final SpringDataMongoBusinessRepository repository;

    public MutationBusinessRepository(TransactionalOrchestrator transaction, SpringDataMongoBusinessRepository repository) {
        this.transaction = transaction;
        this.repository = repository;
    }

    public Result<Void, Error> save(MongoBusinessEntity business) {
        return this.transaction
                .write(() -> {
                    this.repository.save(business);
                    return null;
                });
    }

    public Result<Void, com.jobby.domain.mobility.error.Error> update(MongoBusinessEntity business) {
        return this.transaction
                .read(()-> this.repository.findById(business.getId()))
                .flatMap((op)-> op
                        .map(Result::<MongoBusinessEntity, Error>success)
                        .orElseGet(()-> Result.failure(ErrorType.NOT_FOUND, new Field("business", "business not found")))
                )
                .flatMap(entityFounded -> this.transaction
                        .write(()-> {
                            this.repository.save(entityFounded);
                            return null;
                        }));
    }

}
