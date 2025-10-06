package com.jobby.business.infrastructure.adapters.out;

import com.jobby.business.domain.Business;
import com.jobby.business.domain.ports.BusinessRepository;
import com.jobby.business.infrastructure.persistence.jpa.entities.JpaBusinessEntity;
import com.jobby.business.infrastructure.persistence.jpa.mappers.JpaBusinessMapper;
import com.jobby.business.infrastructure.persistence.jpa.repositories.SpringDataJpaBusinessRepository;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import com.jobby.infraestructure.common.EncryptionPropertyInitializer;
import com.jobby.infraestructure.common.MacPropertyInitializer;
import com.jobby.infraestructure.common.repo.JpaGenericRepository;
import com.jobby.infraestructure.common.transaction.TransactionHandler;
import org.springframework.stereotype.Service;

@Service("write")
public class WriteBusinessRepository
        extends JpaGenericRepository<JpaBusinessEntity, Business>
        implements BusinessRepository {

    private final JpaBusinessMapper jpaBusinessMapper;
    private final MacPropertyInitializer macPropertyInitializer;
    private final SpringDataJpaBusinessRepository springDataJpaBusinessRepository;
    private final EncryptionPropertyInitializer encryptionPropertyInitializer;
    private final TransactionHandler transactionHandler;

    public WriteBusinessRepository(JpaBusinessMapper jpaBusinessMapper, MacPropertyInitializer macPropertyInitializer, SpringDataJpaBusinessRepository springDataJpaBusinessRepository, EncryptionPropertyInitializer encryptionPropertyInitializer, TransactionHandler transactionHandler) {
        this.jpaBusinessMapper = jpaBusinessMapper;
        this.macPropertyInitializer = macPropertyInitializer;
        this.springDataJpaBusinessRepository = springDataJpaBusinessRepository;
        this.encryptionPropertyInitializer = encryptionPropertyInitializer;
        this.transactionHandler = transactionHandler;
    }

    @Override
    public Result<Business, Error> save(Business business) {
        return this.transactionHandler.execute(
                () -> this.modify(business, (jpaBusiness) ->
                        this.macPropertyInitializer
                            .addElement(jpaBusiness.getAddress())
                            .processAll()
                            .flatMap(x ->
                                this.encryptionPropertyInitializer
                                        .addElement(jpaBusiness.getAddress())
                                        .processAll())
                            .map(x -> this.springDataJpaBusinessRepository.saveAndFlush(jpaBusiness))
        ));
    }

    @Override
    public Result<Business, Error> findById(int id) {
        return this.transactionHandler.executeInRead(
                () -> this.select(() -> this.springDataJpaBusinessRepository.findById(id))
        );
    }

    @Override
    protected Business toDomain(JpaBusinessEntity jpaBusinessEntity) {
        return this.jpaBusinessMapper.toDomain(jpaBusinessEntity);
    }

    @Override
    protected JpaBusinessEntity toEntity(Business business) {
        return this.jpaBusinessMapper.toJpa(business);
    }
}
