package com.jobby.authorization.application;

import com.jobby.authorization.domain.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ObtainEmployeesUseCase {

    private final MongoRepository<Employee, Integer> employeeRepository;

    @Autowired
    public ObtainEmployeesUseCase(MongoRepository<Employee, Integer> employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getEmployees() {
        var employees = employeeRepository.findAll();
        return employees;
    }
}
