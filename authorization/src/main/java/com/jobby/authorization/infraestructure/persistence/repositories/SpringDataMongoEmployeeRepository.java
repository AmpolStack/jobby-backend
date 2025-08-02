package com.jobby.authorization.infraestructure.persistence.repositories;

import com.jobby.authorization.infraestructure.persistence.entities.MongoEmployeeEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SpringDataMongoEmployeeRepository extends MongoRepository<MongoEmployeeEntity, Integer> {
    Optional<MongoEmployeeEntity> findByEmailAndPassword(String email, String password);
}
