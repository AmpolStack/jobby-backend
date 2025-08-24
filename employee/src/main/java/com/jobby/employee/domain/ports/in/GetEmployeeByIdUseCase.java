package com.jobby.employee.domain.ports.in;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import com.jobby.employee.domain.model.Employee;

public interface GetEmployeeByIdUseCase {
    Result<Employee, Error> execute(int id);
}
