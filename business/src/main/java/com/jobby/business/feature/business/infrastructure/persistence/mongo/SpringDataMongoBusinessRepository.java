package com.jobby.business.feature.business.infrastructure.persistence.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.Set;

@Repository
public interface SpringDataMongoBusinessRepository extends MongoRepository<MongoBusinessEntity, Integer> {
    Optional<MongoBusinessEntity> findById(int id);
    boolean existsByName(String name);

    Set<MongoBusinessEntity> findByAddressCityId(int addressCityId);

    Set<MongoBusinessEntity> findByAddressCityCountryId(int addressCityCountryId);
}
