package com.jobby.authorization.domain.ports.out.repositories;

import com.jobby.authorization.domain.model.Employee;
import com.jobby.authorization.domain.result.Error;
import com.jobby.authorization.domain.result.Result;

import java.util.Optional;

public interface EmployeeRepository {
    Result<Employee, Error> findByEmailAndPassword(String email, String password);
    Optional<Employee> findById(int id);
}
