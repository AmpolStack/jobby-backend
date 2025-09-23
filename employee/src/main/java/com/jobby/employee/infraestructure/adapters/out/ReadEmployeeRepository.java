package com.jobby.employee.infraestructure.adapters.out;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.error.ErrorType;
import com.jobby.domain.mobility.error.Field;
import com.jobby.domain.mobility.result.Result;
import com.jobby.employee.domain.model.Employee;
import com.jobby.employee.domain.ports.out.EmployeeRepository;
import com.jobby.employee.infraestructure.persistence.mongo.mappers.MongoEmployeeMapper;
import com.jobby.employee.infraestructure.persistence.mongo.repositories.SpringDataMongoEmployeeRepository;
import org.springframework.stereotype.Component;

@Component("readEmployeeRepository")
public class ReadEmployeeRepository implements EmployeeRepository {

    private final SpringDataMongoEmployeeRepository employeeRepository;
    private final MongoEmployeeMapper mongoEmployeeMapper;

    public ReadEmployeeRepository(SpringDataMongoEmployeeRepository employeeRepository, MongoEmployeeMapper mongoEmployeeMapper) {
        this.employeeRepository = employeeRepository;
        this.mongoEmployeeMapper = mongoEmployeeMapper;
    }

    @Override
    public Result<Employee, Error> save(Employee employee) {
        var employeeDocument = this.mongoEmployeeMapper.toDocument(employee);
        var resp =  this.employeeRepository.save(employeeDocument);
        return Result.success(this.mongoEmployeeMapper.toDomain(resp));
    }

    @Override
    public Result<Employee, Error> getEmployeeById(int id) {
        return this.employeeRepository
                .findById(id)
                .map(this.mongoEmployeeMapper::toDomain)
                .map(Result::<Employee, Error>success)
                .orElseGet(()-> Result.failure(
                        ErrorType.USER_NOT_FOUND,
                        new Field("employee", "No employee found with given id")
                ));
    }
}
