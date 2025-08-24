package com.jobby.employee.infraestructure.adapters.out;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.error.ErrorType;
import com.jobby.domain.mobility.error.Field;
import com.jobby.domain.mobility.result.Result;
import com.jobby.domain.mobility.validator.ValidationChain;
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

    @Override
    public Result<Employee, Error> getEmployeeById(int id) {
        return ValidationChain.create()
                .validateGreaterOrEqualsThan(id, 0, "employee-id")
                .build()
                .flatMap(v ->{
                        var res = this.employeeRepository.findById(id);
                        var comp = res.map(this.jpaEmployeeMapper::toDomain);
                        return comp.map(Result::<Employee, Error>success)
                        .orElseGet(()-> Result.failure(
                                ErrorType.USER_NOT_FOUND,
                                new Field("employee", "No employee found with given id")
                        ));
                }
                );
    }
}
