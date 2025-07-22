package com.jobby.authorization.infraestructure.persistence;

import com.jobby.authorization.domain.model.EmployeeEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface EmployeeRepository extends MongoRepository<EmployeeEntity, Integer> {
    Optional<EmployeeEntity> findByEmailAndPassword(String email, String password);
}
