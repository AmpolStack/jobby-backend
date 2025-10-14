package com.jobby.business.infrastructure.adapters.out.repositories;

import com.jobby.business.domain.entities.Business;
import com.jobby.business.domain.ports.out.ReadOnlyBusinessRepository;
import com.jobby.business.infrastructure.persistence.mongo.entities.MongoBusinessEntity;
import com.jobby.infraestructure.repository.orchestation.RepositoryOrchestrator;
import com.jobby.business.infrastructure.persistence.mongo.repositories.SpringDataMongoBusinessRepository;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import org.springframework.stereotype.Repository;

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
        return this.mongoRepositoryOrchestrator.modification(business,
                (jpaBusiness) ->
                    {
                        this.springDataMongoBusinessRepository.save(jpaBusiness);
                        return null;
                    }
                );
    }

    @Override
    public Result<Business, Error> findById(int id) {
        return this.mongoRepositoryOrchestrator
                .selection(()-> this.springDataMongoBusinessRepository.findById(id));
    }

    @Override
    public Result<Boolean, Error> existByUsername(String name) {
        return this.mongoRepositoryOrchestrator
                .operation(()-> this.springDataMongoBusinessRepository.existsByName(name));
    }
}
