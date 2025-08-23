package com.jobby.employee.infraestructure.adapters.out;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import com.jobby.employee.domain.model.Employee;
import com.jobby.employee.domain.ports.EmployeeRepository;
import com.jobby.employee.infraestructure.persistence.mappers.JpaEmployeeMapper;
import com.jobby.employee.infraestructure.persistence.repositories.SpringDataJpaEmployeeRepository;
import org.springframework.stereotype.Component;

@Component
public class DefaultEmployeeRepository implements EmployeeRepository {
    private final SpringDataJpaEmployeeRepository employeeRepository;
    private final JpaEmployeeMapper jpaEmployeeMapper;

    public DefaultEmployeeRepository(SpringDataJpaEmployeeRepository employeeRepository, JpaEmployeeMapper jpaEmployeeMapper) {
        this.employeeRepository = employeeRepository;
        this.jpaEmployeeMapper = jpaEmployeeMapper;
    }

    @Override
    public Result<Employee, Error> save(Employee employee) {
        return null;
    }
}
