package com.jobby.employee.application.useCases;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import com.jobby.domain.ports.SafeResultValidator;
import com.jobby.employee.domain.model.Employee;
import com.jobby.employee.domain.ports.in.PutEmployeeUseCase;
import com.jobby.employee.domain.ports.out.EmployeeRepository;
import org.springframework.stereotype.Service;

@Service
public class DefaultPutEmployeeUseCase implements PutEmployeeUseCase {

    private final EmployeeRepository employeeRepository;
    private final SafeResultValidator safeResultValidator;

    public DefaultPutEmployeeUseCase(EmployeeRepository employeeRepository, SafeResultValidator safeResultValidator) {
        this.employeeRepository = employeeRepository;
        this.safeResultValidator = safeResultValidator;
    }

    @Override
    public Result<Employee, Error> execute(Employee employee) {
        return this.safeResultValidator.validate(employee)
                .flatMap(x -> this.employeeRepository.save(employee));
    }
}
