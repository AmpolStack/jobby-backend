package com.jobby.authorization.domain.ports.out.repositories;

import com.jobby.authorization.domain.model.Employee;
import java.util.Optional;

public interface EmployeeRepository {
    Optional<Employee> findByEmailAndPassword(String email, String password);
    Optional<Employee> findById(int id);
}
