package com.jobby.business.infrastructure.persistence.mongo.repositories;

import com.jobby.business.infrastructure.persistence.mongo.entities.MongoBusinessEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataMongoBusinessRepository extends MongoRepository<MongoBusinessEntity, Integer> {
}
