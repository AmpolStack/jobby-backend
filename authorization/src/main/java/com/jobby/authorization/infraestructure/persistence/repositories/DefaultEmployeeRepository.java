package com.jobby.authorization.infraestructure.persistence.repositories;

import com.jobby.authorization.domain.model.Employee;
import com.jobby.authorization.domain.ports.EmployeeRepository;
import com.jobby.authorization.infraestructure.persistence.mappers.MongoEmployeeEntityMapper;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class DefaultEmployeeRepository implements EmployeeRepository {

    private final MongoEmployeeEntityMapper mongoMapper;
    private final SpringDataMongoEmployeeRepository mongoRepository;

    public DefaultEmployeeRepository(MongoEmployeeEntityMapper mongoMapper, SpringDataMongoEmployeeRepository mongoRepository) {
        this.mongoMapper = mongoMapper;
        this.mongoRepository = mongoRepository;
    }

    @Override
    public Optional<Employee> findByEmailAndPassword(String email, String password) {
        return this.mongoRepository
                .findByEmailAndPassword(email, password)
                .map(this.mongoMapper::toDomain);
    }

}
