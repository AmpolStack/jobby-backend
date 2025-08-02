package com.jobby.authorization.application.useCases;

import com.jobby.authorization.domain.model.Employee;
import com.jobby.authorization.domain.ports.EmployeeRepository;
import org.springframework.stereotype.Service;

@Service
public class ObtainEmployeesUseCase {

    private final EmployeeRepository employeeRepository;

    public ObtainEmployeesUseCase(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee getEmployee(String email, String password) {
        return this.employeeRepository.findByEmailAndPassword(email, password).orElse(null);
    }

}
