package com.jobby.business.infrastructure.adapters.out.repositories;

import com.jobby.business.domain.entities.Business;
import com.jobby.business.domain.ports.out.BusinessRepository;
import com.jobby.infraestructure.repository.pipeline.AfterPersistProcess;
import com.jobby.infraestructure.repository.pipeline.BeforePersistProcess;
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
    private final BeforePersistProcess<JpaBusinessEntity, Business> beforePersistProcess;
    private final AfterPersistProcess<JpaBusinessEntity, Business> afterPersistProcess;

    public DefaultBusinessRepository(SpringDataMongoBusinessRepository springDataMongoBusinessRepository,
                                     SpringDataJpaBusinessRepository springDataJpaBusinessRepository,
                                     BeforePersistProcess<JpaBusinessEntity, Business> beforePersistProcess,
                                     AfterPersistProcess<JpaBusinessEntity, Business> afterPersistProcess) {
        this.springDataMongoBusinessRepository = springDataMongoBusinessRepository;
        this.springDataJpaBusinessRepository = springDataJpaBusinessRepository;
        this.beforePersistProcess = beforePersistProcess;
        this.afterPersistProcess = afterPersistProcess;
    }

    @Override
    public Result<Business, Error> save(Business business) {
        return this.beforePersistProcess.before(business)
                .map(jpaBusiness -> {
                    var savedBusiness = this.springDataJpaBusinessRepository.save(jpaBusiness);
                    return this.springDataJpaBusinessRepository.findById(savedBusiness.getId());
                })
                .flatMap(this.afterPersistProcess::after);
    }

    @Override
    public Result<Business, Error> findById(int id) {
        var x = this.springDataMongoBusinessRepository.findById(id);
        return null;
    }

    @Override
    public Result<Boolean, Error> existByUsername(String name) {
        return null;
    }
}
