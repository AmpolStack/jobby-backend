package com.jobby.authorization.infraestructure.persistence.repositories;

import com.jobby.authorization.domain.model.Employee;
import com.jobby.authorization.domain.ports.EmployeeRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class EmployeeRepositoryImpl implements EmployeeRepository {

    private final SpringDataMongoEmployeeRepository mongoRepository;

    public EmployeeRepositoryImpl(SpringDataMongoEmployeeRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public Optional<Employee> findByEmailAndPassword(String email, String password) {
        return Optional.empty();
    }
}
