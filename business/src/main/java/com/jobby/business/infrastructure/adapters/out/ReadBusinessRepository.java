package com.jobby.business.infrastructure.adapters.out;

import com.jobby.business.domain.entities.Business;
import com.jobby.business.domain.ports.out.BusinessRepository;
import com.jobby.business.infrastructure.persistence.mongo.entities.MongoBusinessEntity;
import com.jobby.business.infrastructure.persistence.mongo.mappers.MongoBusinessMapper;
import com.jobby.business.infrastructure.persistence.mongo.repositories.SpringDataMongoBusinessRepository;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import com.jobby.infraestructure.common.EncryptionPropertyInitializer;
import com.jobby.infraestructure.common.MacPropertyInitializer;
import com.jobby.infraestructure.common.repo.MongoGenericRepository;
import com.jobby.infraestructure.common.transaction.TransactionHandler;
import org.springframework.stereotype.Service;

@Service("read")
public class ReadBusinessRepository
        extends MongoGenericRepository<MongoBusinessEntity, Business>
        implements BusinessRepository {

    private final MongoBusinessMapper mongoBusinessMapper;
    private final SpringDataMongoBusinessRepository springDataMongoBusinessRepository;
    private final MacPropertyInitializer macPropertyInitializer;
    private final TransactionHandler transactionHandler;
    private final EncryptionPropertyInitializer encryptionPropertyInitializer;

    public ReadBusinessRepository(MongoBusinessMapper mongoBusinessMapper, SpringDataMongoBusinessRepository springDataMongoBusinessRepository, EncryptionPropertyInitializer encryptionPropertyInitializer, MacPropertyInitializer macPropertyInitializer, TransactionHandler transactionHandler, EncryptionPropertyInitializer encryptionPropertyInitializer1) {
        this.mongoBusinessMapper = mongoBusinessMapper;
        this.springDataMongoBusinessRepository = springDataMongoBusinessRepository;
        this.macPropertyInitializer = macPropertyInitializer;
        this.transactionHandler = transactionHandler;
        this.encryptionPropertyInitializer = encryptionPropertyInitializer1;
    }

    @Override
    public Result<Business, Error> save(Business business) {
        return this.transactionHandler.execute(
                () -> this.modify(business, (jpaBusiness) ->
                        this.macPropertyInitializer
                                .addElement(jpaBusiness.getAddress())
                                .processAll()
                                .map(x -> this.springDataMongoBusinessRepository.save(jpaBusiness))
                ));
    }

    @Override
    public Result<Business, Error> findById(int id) {
        return null;
    }

    @Override
    public Result<Business, Error> findByAddress_ValueSearchable(byte[] addressValueSearchable) {
        return this.transactionHandler.executeInRead(
                () -> this.select(() -> this.springDataMongoBusinessRepository.findByAddress_ValueSearchable(addressValueSearchable),
                        (jpaBusiness) -> this.macPropertyInitializer
                                .addElement(jpaBusiness.getAddress())
                                .processAll()
                                .flatMap(x ->
                                        this.encryptionPropertyInitializer
                                                .addElement(jpaBusiness.getAddress())
                                                .processAll()))
        );
    }

    @Override
    protected Business toDomain(MongoBusinessEntity entity) {
        return this.mongoBusinessMapper.toDomain(entity);
    }

    @Override
    protected MongoBusinessEntity toEntity(Business business) {
        return this.mongoBusinessMapper.toDocument(business);
    }
}
