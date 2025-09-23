package com.jobby.employee.application.events;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import com.jobby.employee.domain.model.Employee;
import com.jobby.employee.domain.ports.in.CreateEmployeeFromEventUseCase;
import com.jobby.employee.domain.ports.out.EmployeeRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class DefaultCreateEmployeeFromEventUseCase implements CreateEmployeeFromEventUseCase {

    private final EmployeeRepository employeeRepository;

    public DefaultCreateEmployeeFromEventUseCase(@Qualifier("readEmployeeRepository") EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Result<Employee, Error> execute(Employee employee) {
        return this.employeeRepository.save(employee);
    }
}
