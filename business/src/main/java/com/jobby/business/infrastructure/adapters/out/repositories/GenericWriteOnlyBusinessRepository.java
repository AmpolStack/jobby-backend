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
    public Result<Void, Error> delete(int id) {
        return this.jpaRepositoryOrchestrator.onModify(
                () -> {
                    this.springDataJpaBusinessRepository.deleteById(id);
                    return null;
                });
    }

    @Override
    public Result<Business, Error> update(Business business) {
        return this.jpaRepositoryOrchestrator.onModify(business,
                        this.springDataJpaBusinessRepository::save)
                .map(v -> business);
    }

    @Override
    public Result<Business, Error> updatePictures(int id, String bannerImageUrl, String profileImageUrl) {
        return this.jpaRepositoryOrchestrator.onModify(() -> {
            this.springDataJpaBusinessRepository.updatePictures(id, bannerImageUrl, profileImageUrl);
            return null;
        }).flatMap(v -> this.findById(id));
    }

    @Override
    public Result<Business, Error> updateName(int id, String name) {
        return this.jpaRepositoryOrchestrator.onModify(() -> {
            this.springDataJpaBusinessRepository.updateName(id, name);
            return null;
        }).flatMap(v -> this.findById(id));
    }

    @Override
    public Result<Business, Error> updateDescription(int id, String description) {
        return this.jpaRepositoryOrchestrator.onModify(() -> {
            this.springDataJpaBusinessRepository.updateDescription(id, description);
            return null;
        }).flatMap(v -> this.findById(id));
    }

    @Override
    public Result<Business, Error> updateNameAndDescription(int id, String name, String description) {
        return this.jpaRepositoryOrchestrator.onModify(() -> {
            this.springDataJpaBusinessRepository.updateNameAndDescription(id, name, description);
            return null;
        }).flatMap(v -> this.findById(id));
    }
}
