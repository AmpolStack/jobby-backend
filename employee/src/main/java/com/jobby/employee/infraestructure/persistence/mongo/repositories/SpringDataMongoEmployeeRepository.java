package com.jobby.employee.infraestructure.persistence.mongo.repositories;

import com.jobby.employee.infraestructure.persistence.mongo.entities.MongoEmployeeEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataMongoEmployeeRepository extends MongoRepository<MongoEmployeeEntity, Integer> {
}
