package com.jobby.employee.application.useCases;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import com.jobby.domain.mobility.validator.ValidationChain;
import com.jobby.employee.domain.model.Employee;
import com.jobby.employee.domain.ports.in.GetEmployeeByIdUseCase;
import com.jobby.employee.domain.ports.out.EmployeeRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class DefaultGetEmployeeByIdUseCase implements GetEmployeeByIdUseCase {

    private final EmployeeRepository employeeRepository;

    public DefaultGetEmployeeByIdUseCase(@Qualifier("writeEmployeeRepository") EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Result<Employee, Error> execute(int id) {
        return ValidationChain
                .create()
                .validateGreaterThan(id, 0, "employee-id")
                .build()
                .flatMap(v -> this.employeeRepository.getEmployeeById(id));
    }
}
