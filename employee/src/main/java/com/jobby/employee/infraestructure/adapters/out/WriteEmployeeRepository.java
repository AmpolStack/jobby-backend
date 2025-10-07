package com.jobby.employee.infraestructure.adapters.out;

import com.jobby.domain.configurations.MacConfig;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.error.ErrorType;
import com.jobby.domain.mobility.error.Field;
import com.jobby.domain.mobility.result.Result;
import com.jobby.domain.ports.hashing.mac.MacService;
import com.jobby.employee.domain.model.Employee;
import com.jobby.employee.domain.ports.out.EmployeeRepository;
import com.jobby.employee.infraestructure.persistence.jpa.mappers.JpaEmployeeMapper;
import com.jobby.employee.infraestructure.persistence.jpa.repositories.SpringDataJpaEmployeeRepository;
import com.jobby.infraestructure.common.security.mac.MacPropertyInitializer;
import org.springframework.stereotype.Component;

@Component("writeEmployeeRepository")
public class WriteEmployeeRepository implements EmployeeRepository {
    private final SpringDataJpaEmployeeRepository employeeRepository;
    private final JpaEmployeeMapper jpaEmployeeMapper;
    private final MacPropertyInitializer macPropertyInitializer;

    public WriteEmployeeRepository(SpringDataJpaEmployeeRepository employeeRepository, JpaEmployeeMapper jpaEmployeeMapper, MacConfig macConfig, MacService macService, MacPropertyInitializer macPropertyInitializer) {
        this.employeeRepository = employeeRepository;
        this.jpaEmployeeMapper = jpaEmployeeMapper;
        this.macPropertyInitializer = macPropertyInitializer;
    }

    @Override
    public Result<Employee, Error> save(Employee employee) {
        var jpaEmployeeEntity = this.jpaEmployeeMapper.toJpa(employee);
        return this.macPropertyInitializer.addElement(jpaEmployeeEntity)
                .processAll()
                .flatMap(v ->{
                    try{
                        var resp = this.employeeRepository.save(jpaEmployeeEntity);
                        return Result.success(this.jpaEmployeeMapper.toDomain(resp));
                    }
                    catch (Exception ex){
                        return Result.failure(ErrorType.ITS_EXTERNAL_SERVICE_FAILURE,
                                new Field("jpa database", ex.getMessage()));
                    }
                });
    }

    @Override
    public Result<Employee, Error> getEmployeeById(int id) {
        return this.employeeRepository
                .findById(id)
                .map(this.jpaEmployeeMapper::toDomain)
                .map(Result::<Employee, Error>success)
                .orElseGet(()-> Result.failure(
                        ErrorType.USER_NOT_FOUND,
                        new Field("employee", "No employee found with given id")
                ));
    }


}
