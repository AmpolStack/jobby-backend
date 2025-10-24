package com.jobby.business.infrastructure.persistence.business.mongo.repositories;

import com.jobby.business.infrastructure.persistence.business.mongo.entities.MongoBusinessEntity;
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
