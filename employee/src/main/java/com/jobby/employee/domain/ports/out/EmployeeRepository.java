package com.jobby.employee.domain.ports.out;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import com.jobby.employee.domain.model.Employee;

public interface EmployeeRepository {
    Result<Employee, Error> save(Employee employee);
    Result<Employee, Error> getEmployeeById(int id);
}
