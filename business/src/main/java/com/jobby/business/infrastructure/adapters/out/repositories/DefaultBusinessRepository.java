package com.jobby.business.infrastructure.adapters.out.repositories;

import com.jobby.business.domain.entities.Business;
import com.jobby.business.domain.ports.out.BusinessRepository;
import com.jobby.business.infrastructure.persistence.mongo.entities.MongoBusinessEntity;
import com.jobby.infraestructure.repository.orchestation.RepositoryOrchestrator;
import com.jobby.business.infrastructure.persistence.jpa.entities.JpaBusinessEntity;
import com.jobby.business.infrastructure.persistence.jpa.repositories.SpringDataJpaBusinessRepository;
import com.jobby.business.infrastructure.persistence.mongo.repositories.SpringDataMongoBusinessRepository;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import org.springframework.stereotype.Service;

@Service
public class DefaultBusinessRepository implements BusinessRepository {

    private final SpringDataMongoBusinessRepository springDataMongoBusinessRepository;
    private final SpringDataJpaBusinessRepository springDataJpaBusinessRepository;
    private final RepositoryOrchestrator<JpaBusinessEntity, Business> jpaRepositoryOrchestrator;
    private final RepositoryOrchestrator<MongoBusinessEntity, Business> mongoRepositoryOrchestrator;

    public DefaultBusinessRepository(SpringDataMongoBusinessRepository springDataMongoBusinessRepository, SpringDataJpaBusinessRepository springDataJpaBusinessRepository, RepositoryOrchestrator<JpaBusinessEntity, Business> jpaRepositoryOrchestrator, RepositoryOrchestrator<MongoBusinessEntity, Business> mongoRepositoryOrchestrator) {
        this.springDataMongoBusinessRepository = springDataMongoBusinessRepository;
        this.springDataJpaBusinessRepository = springDataJpaBusinessRepository;
        this.jpaRepositoryOrchestrator = jpaRepositoryOrchestrator;
        this.mongoRepositoryOrchestrator = mongoRepositoryOrchestrator;
    }

    @Override
    public Result<Business, Error> save(Business business) {
        return this.jpaRepositoryOrchestrator.modification(business,
                (jpaBusiness)
                        -> this.springDataJpaBusinessRepository.save(jpaBusiness).getId())
                .flatMap((savedBusinessId)
                        -> this.jpaRepositoryOrchestrator.selection(
                                ()-> this.springDataJpaBusinessRepository.findById(savedBusinessId)));
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
