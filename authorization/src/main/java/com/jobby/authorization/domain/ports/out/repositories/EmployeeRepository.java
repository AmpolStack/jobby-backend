package com.jobby.authorization.domain.ports.out.repositories;

import com.jobby.authorization.domain.model.Employee;
import com.jobby.domain.mobility.Error;
import com.jobby.domain.result.Result;

public interface EmployeeRepository {
    Result<Employee, Error> findByEmailAndPassword(String email, String password);
    Result<Employee, Error> findById(int id);
}
