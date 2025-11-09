package com.jobby.business.feature.business.infrastructure.adapters.out.repositories;

import com.jobby.business.feature.business.domain.entities.Business;
import com.jobby.business.feature.business.domain.ports.out.repositories.WriteOnlyBusinessRepository;
import com.jobby.business.feature.business.infrastructure.internal.BusinessCacheFinder;
import com.jobby.business.feature.business.infrastructure.internal.BusinessPublisher;
import com.jobby.infraestructure.transaction.orchetration.TransactionalOrchestrator;
import com.jobby.business.feature.business.infrastructure.persistence.jpa.JpaBusinessEntity;
import com.jobby.business.feature.business.infrastructure.persistence.jpa.JpaBusinessMapper;
import com.jobby.business.feature.business.infrastructure.persistence.jpa.SpringDataJpaBusinessRepository;
import com.jobby.infraestructure.security.SecuredPropertyTransformer;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.error.ErrorType;
import com.jobby.domain.mobility.error.Field;
import com.jobby.domain.mobility.result.Result;
import com.jobby.domain.ports.SafeResultValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.function.Consumer;

@Repository
@AllArgsConstructor
public class WriteOnlyBusinessRepositoryImpl implements WriteOnlyBusinessRepository {

    private final SpringDataJpaBusinessRepository repository;
    private final BusinessCacheFinder cache;
    private final BusinessPublisher publisher;
    private final TransactionalOrchestrator transaction;
    private final SecuredPropertyTransformer transformer;
    private final JpaBusinessMapper mapper;
    private final SafeResultValidator validator;

    @Override
    public Result<Business, Error> save(Business business) {
        var mapped = this.mapper.toJpa(business);
        return this.transformer
                    .addProperty(mapped.getAddress().getValue())
                .apply()
                .flatMap(v -> this.validator.validate(mapped))
                .flatMap(v -> this.transaction
                        .write(() -> this.repository.save(mapped)))
                .flatMap(entity -> this.transaction.read(()-> this.repository.findById(entity.getId())))
                .flatMap(op -> {
                    @SuppressWarnings("OptionalGetWithoutIsPresent")
                    var entity = op.get();
                    return this.transformer.addProperty(entity.getAddress().getValue())
                            .revert()
                            .flatMap(v -> {
                                var businessFounded = this.mapper.toDomain(entity);
                                return this.publisher.prepare(businessFounded)
                                        .map(bytes -> {
                                            this.publisher.send(bytes, businessFounded);
                                            return businessFounded;
                                        });
                            });
                });
    }

    @Override
    public Result<Void, Error> delete(int id) {
        return this.transaction
                .read(()-> this.repository.findById(id))
                .flatMap((op)-> op
                            .map(Result::<JpaBusinessEntity, Error>success)
                            .orElseGet(()-> Result.failure(ErrorType.NOT_FOUND, new Field("business", "business not found")))
                )
                .flatMap(v -> this.transaction
                        .write(()-> {
                            this.repository.deleteById(id);
                            return null;
                        }))
                .map(v -> {
                    this.cache.deleteRegistry(id);
                    return null;
                });
    }

    @Override
    public Result<Business, Error> updatePictures(int id, String bannerImageUrl, String profileImageUrl) {
        return update(id,
                (jpa)-> {
                    jpa.setBannerImageUrl(bannerImageUrl);
                    jpa.setProfileImageUrl(profileImageUrl);
                });
    }

    @Override
    public Result<Business, Error> updateName(int id, String name) {
        return update(id,
                (jpa)-> jpa.setName(name));
    }

    @Override
    public Result<Business, Error> updateDescription(int id, String description) {
        return update(id,
                (jpa)-> jpa.setDescription(description));
    }

    @Override
    public Result<Business, Error> updateNameAndDescription(int id, String name, String description) {
        return update(id,
                (jpa)-> {
                    jpa.setName(name);
                    jpa.setDescription(description);
                });
    }

    private Result<Business,Error> update(
            int id,
            Consumer<JpaBusinessEntity> consumer)
    {
        return this.transaction
                .read(()-> this.repository.findById(id))
                .flatMap((op)-> op
                        .map(Result::<JpaBusinessEntity, Error>success)
                        .orElseGet(()-> Result.failure(ErrorType.NOT_FOUND, new Field("business", "business not found")))
                )
                .flatMap(entityFounded -> {
                    consumer.accept(entityFounded);
                    return this.transformer
                            .addProperty(entityFounded.getAddress().getValue())
                            .revert()
                            .flatMap((v)-> {
                                var mapped = this.mapper.toDomain(entityFounded);

                                return this.transaction
                                        .triggers()
                                        .add(this.publisher, mapped)
                                        .add(()-> this.cache.deleteRegistry(id))
                                        .build()
                                        .write(()-> this.repository.save(entityFounded))
                                        .map(this.mapper::toDomain);
                            });

                });
    }

}
