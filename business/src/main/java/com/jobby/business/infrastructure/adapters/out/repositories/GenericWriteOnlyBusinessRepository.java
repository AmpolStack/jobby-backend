package com.jobby.business.infrastructure.adapters.out.repositories;

import com.jobby.business.domain.entities.Business;
import com.jobby.business.domain.ports.out.repositories.WriteOnlyBusinessRepository;
import com.jobby.business.infrastructure.persistence.jpa.entities.JpaBusinessEntity;
import com.jobby.business.infrastructure.persistence.jpa.repositories.SpringDataJpaBusinessRepository;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import com.jobby.infraestructure.repository.orchestation.RepositoryOrchestrator;
import org.springframework.stereotype.Repository;

@Repository
public class GenericWriteOnlyBusinessRepository implements WriteOnlyBusinessRepository {

    private final SpringDataJpaBusinessRepository springDataJpaBusinessRepository;
    private final RepositoryOrchestrator<JpaBusinessEntity, Business> jpaRepositoryOrchestrator;

    public GenericWriteOnlyBusinessRepository(SpringDataJpaBusinessRepository springDataJpaBusinessRepository, RepositoryOrchestrator<JpaBusinessEntity, Business> jpaRepositoryOrchestrator) {
        this.springDataJpaBusinessRepository = springDataJpaBusinessRepository;
        this.jpaRepositoryOrchestrator = jpaRepositoryOrchestrator;
    }

    @Override
    public Result<Business, Error> save(Business business) {
        return this.jpaRepositoryOrchestrator.onModify(business,
                        (jpaBusiness)
                                -> this.springDataJpaBusinessRepository.save(jpaBusiness).getId())
                .flatMap((savedBusinessId)
                        -> this.jpaRepositoryOrchestrator.onSelect(
                        ()-> this.springDataJpaBusinessRepository.findById(savedBusinessId)));
    }


    @Override
    public Result<Business, Error> findById(int id) {
        return this.jpaRepositoryOrchestrator.onSelect(
                () -> this.springDataJpaBusinessRepository.findById(id));
    }

    @Override
    public Result<Void, Error> delete(Business business) {
        return this.jpaRepositoryOrchestrator.onModify(business,
                (v) -> {
                    this.springDataJpaBusinessRepository.delete(v);
                    return null;
                });
    }

    @Override
    public Result<Business, Error> update(Business business) {
        return this.jpaRepositoryOrchestrator.onModify(business,
                        this.springDataJpaBusinessRepository::save)
                .map(v -> business);
    }
}
