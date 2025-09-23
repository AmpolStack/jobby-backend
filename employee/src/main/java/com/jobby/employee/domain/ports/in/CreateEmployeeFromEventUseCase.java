package com.jobby.employee.domain.ports.in;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import com.jobby.employee.domain.model.Employee;

public interface CreateEmployeeFromEventUseCase {
    Result<Employee, Error> execute(Employee employee);
}
