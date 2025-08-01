package com.jobby.authorization.domain.ports;

import com.jobby.authorization.domain.model.Employee;
import java.util.Optional;

public interface EmployeeRepository {
    Optional<Employee> findByEmailAndPassword(String email, String password);
}
