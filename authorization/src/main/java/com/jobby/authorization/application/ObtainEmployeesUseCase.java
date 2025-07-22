package com.jobby.authorization.application;

import com.jobby.authorization.domain.model.EmployeeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ObtainEmployeesUseCase {

    private final MongoRepository<EmployeeEntity, Integer> employeeRepository;

    @Autowired
    public ObtainEmployeesUseCase(MongoRepository<EmployeeEntity, Integer> employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<EmployeeEntity> getEmployees() {
        var employees = employeeRepository.findAll();
        return employees;
    }
}
