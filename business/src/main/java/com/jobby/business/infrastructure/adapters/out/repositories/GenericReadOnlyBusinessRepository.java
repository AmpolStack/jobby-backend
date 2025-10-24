package com.jobby.business.infrastructure.adapters.out.repositories;

import com.jobby.business.domain.entities.Business;
import com.jobby.business.domain.ports.out.repositories.ReadOnlyBusinessRepository;
import com.jobby.business.infrastructure.persistence.business.mongo.entities.MongoBusinessEntity;
import com.jobby.infraestructure.repository.orchestation.RepositoryOrchestrator;
import com.jobby.business.infrastructure.persistence.business.mongo.repositories.SpringDataMongoBusinessRepository;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public class GenericReadOnlyBusinessRepository implements ReadOnlyBusinessRepository {

    private final SpringDataMongoBusinessRepository springDataMongoBusinessRepository;
    private final RepositoryOrchestrator<MongoBusinessEntity, Business> mongoRepositoryOrchestrator;

    public GenericReadOnlyBusinessRepository(SpringDataMongoBusinessRepository springDataMongoBusinessRepository,
                                             RepositoryOrchestrator<MongoBusinessEntity, Business> mongoRepositoryOrchestrator) {
        this.springDataMongoBusinessRepository = springDataMongoBusinessRepository;
        this.mongoRepositoryOrchestrator = mongoRepositoryOrchestrator;
    }

    @Override
    public Result<Void, Error> save(Business business) {
        return this.mongoRepositoryOrchestrator.onModify(business,
                (jpaBusiness) ->
                    {
                        this.springDataMongoBusinessRepository.save(jpaBusiness);
                        return null;
                    }
                );
    }

    @Override
    public Result<Void, Error> update(Business business) {
        return this.mongoRepositoryOrchestrator.onModify(business,
                (mongoBusiness) -> {
                    this.springDataMongoBusinessRepository.save(mongoBusiness);
                    return null;
                });
    }


    @Override
    public Result<Business, Error> findById(int id) {
        return this.mongoRepositoryOrchestrator
                .onSelect(()-> this.springDataMongoBusinessRepository.findById(id));
    }

    @Override
    public Result<Boolean, Error> existByUsername(String name) {
        return this.mongoRepositoryOrchestrator
                .onOperation(()-> this.springDataMongoBusinessRepository.existsByName(name));
    }

    @Override
    public Result<Set<Business>, Error> findByCityId(int cityId) {
        return this.mongoRepositoryOrchestrator.onSelectSet(
                () -> this.springDataMongoBusinessRepository.findByAddressCityId((cityId)));
    }

    @Override
    public Result<Set<Business>, Error> findByCountryId(int countryId) {
        return this.mongoRepositoryOrchestrator.onSelectSet(
                () -> this.springDataMongoBusinessRepository.findByAddressCityCountryId((countryId)));
    }

}
