package com.jobby.business.infrastructure.adapters.out.repositories;

import com.jobby.business.domain.entities.Business;
import com.jobby.business.domain.ports.out.repositories.ReadOnlyBusinessRepository;
import com.jobby.infraestructure.transaction.orchetration.TransactionalOrchestrator;
import com.jobby.business.infrastructure.persistence.business.mongo.entities.MongoBusinessEntity;
import com.jobby.business.infrastructure.persistence.business.mongo.mappers.MongoBusinessMapper;
import com.jobby.business.infrastructure.persistence.business.jpa.security.SecuredPropertyTransformer;
import com.jobby.domain.mobility.error.ErrorType;
import com.jobby.domain.mobility.error.Field;
import com.jobby.domain.ports.SafeResultValidator;
import com.jobby.business.infrastructure.persistence.business.mongo.repositories.SpringDataMongoBusinessRepository;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

@Repository
@AllArgsConstructor
public class ReadOnlyBusinessRepositoryImpl implements ReadOnlyBusinessRepository {

    private final SpringDataMongoBusinessRepository springDataMongoBusinessRepository;
    private final TransactionalOrchestrator transactionalOrchestrator;
    private final MongoBusinessMapper mapper;
    private final SecuredPropertyTransformer transformer;
    private final SafeResultValidator validator;

    @Override
    public Result<Void, Error> save(Business business) {
        var mapped = this.mapper.toDocument(business);
        return this.transformer
                .addProperty(mapped.getAddress().getValue())
                .apply()
                .flatMap(v -> this.validator.validate(mapped))
                .flatMap(v -> this.transactionalOrchestrator
                        .write(() -> {
                            this.springDataMongoBusinessRepository.save(mapped);
                            return null;
                        }));
    }

    @Override
    public Result<Void, Error> update(Business business) {
        return this.transactionalOrchestrator
                .read(()-> this.springDataMongoBusinessRepository.findById(business.getId()))
                .flatMap((op)-> op
                        .map(Result::<MongoBusinessEntity, Error>success)
                        .orElseGet(()-> Result.failure(ErrorType.NOT_FOUND, new Field("business", "business not found")))
                )
                .flatMap(entityFounded -> this.transformer
                        .addProperty(entityFounded.getAddress().getValue())
                        .revert()
                        .flatMap((v)-> this.transactionalOrchestrator
                                .write(()-> {
                                    this.springDataMongoBusinessRepository.save(entityFounded);
                                    return null;
                                })));
    }


    @Override
    public Result<Business, Error> findById(int id) {
        return select(()-> this.springDataMongoBusinessRepository.findById(id));
    }


    @Override
    public Result<Set<Business>, Error> findByCityId(int cityId) {
        return selectSet(() -> this.springDataMongoBusinessRepository.findByAddressCityId(cityId));
    }

    @Override
    public Result<Set<Business>, Error> findByCountryId(int countryId) {
        return selectSet(()-> this.springDataMongoBusinessRepository.findByAddressCityCountryId(countryId));
    }

    private Result<Business,Error> select(Supplier<Optional<MongoBusinessEntity>> supplier) {
        return this.transactionalOrchestrator
                .read(supplier)
                .flatMap(op ->
                        op.map((entity)->
                                        transformer
                                                .addProperty(entity.getAddress().getValue())
                                                .revert()
                                                .map((v)-> this.mapper.toDomain(entity)))
                                .orElseGet(()-> Result.failure(
                                                ErrorType.NOT_FOUND,
                                                new Field("business", "business not found")
                                        )
                                ));
    }

    private Result<Set<Business>,Error> selectSet(Supplier<Set<MongoBusinessEntity>> supplier) {
        return this.transactionalOrchestrator
                .read(supplier)
                .flatMap(set ->{
                    for (MongoBusinessEntity businessEntity : set) {
                        var transform = transformer
                                .addProperty(businessEntity.getAddress().getValue())
                                .revert();

                        if(transform.isFailure()){
                            return Result.renewFailure(transform);
                        }
                    }

                    return Result.success(set);
                })
                .map(this.mapper::toDomain);
    }

}
