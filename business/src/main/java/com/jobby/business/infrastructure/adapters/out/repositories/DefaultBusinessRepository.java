package com.jobby.business.infrastructure.adapters.out.repositories;

import com.jobby.business.domain.entities.Business;
import com.jobby.business.domain.ports.out.BusinessRepository;
import com.jobby.business.infrastructure.common.AfterPersistProcess;
import com.jobby.business.infrastructure.common.BeforePersistProcess;
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
    private final BeforePersistProcess<Business, JpaBusinessEntity> beforePersistProcess;
    private final AfterPersistProcess<Business, JpaBusinessEntity> afterPersistProcess;

    public DefaultBusinessRepository(SpringDataMongoBusinessRepository springDataMongoBusinessRepository,
                                     SpringDataJpaBusinessRepository springDataJpaBusinessRepository,
                                     BeforePersistProcess<Business, JpaBusinessEntity> beforePersistProcess,
                                     AfterPersistProcess<Business, JpaBusinessEntity> afterPersistProcess) {
        this.springDataMongoBusinessRepository = springDataMongoBusinessRepository;
        this.springDataJpaBusinessRepository = springDataJpaBusinessRepository;
        this.beforePersistProcess = beforePersistProcess;
        this.afterPersistProcess = afterPersistProcess;
    }

    @Override
    public Result<Business, Error> save(Business business) {
        return this.beforePersistProcess.use(business)
                .map(jpaBusiness -> {
                    var savedBusiness = this.springDataJpaBusinessRepository.save(jpaBusiness);
                    return this.springDataJpaBusinessRepository.findById(savedBusiness.getId());
                })
                .flatMap(this.afterPersistProcess::use);
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
