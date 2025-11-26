package com.jobby.employee.infraestructure.adapters.out;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.error.ErrorType;
import com.jobby.domain.mobility.error.Field;
import com.jobby.domain.mobility.result.Result;
import com.jobby.employee.domain.model.Employee;
import com.jobby.employee.domain.ports.out.EmployeeRepository;
import com.jobby.employee.infraestructure.persistence.jpa.mappers.JpaEmployeeMapper;
import com.jobby.employee.infraestructure.persistence.jpa.repositories.SpringDataJpaEmployeeRepository;
import com.jobby.employee.infraestructure.persistence.outbox.EmployeePublisher;
import com.jobby.infraestructure.security.SecuredPasswordTransformer;
import com.jobby.infraestructure.security.SecuredPropertyTransformer;
import com.jobby.infraestructure.transaction.orchetration.TransactionalOrchestrator;
import org.springframework.stereotype.Component;

@Component("writeEmployeeRepository")
public class WriteEmployeeRepository implements EmployeeRepository {
    private final SpringDataJpaEmployeeRepository repository;
    private final JpaEmployeeMapper mapper;
    private final TransactionalOrchestrator transaction;
    private final SecuredPropertyTransformer propertyTransformer;
    private final SecuredPasswordTransformer passwordTransformer;
    private final EmployeePublisher publisher;

    public WriteEmployeeRepository(SpringDataJpaEmployeeRepository employeeRepository,
                                   JpaEmployeeMapper jpaEmployeeMapper, TransactionalOrchestrator transaction, SecuredPropertyTransformer propertyTransformer, SecuredPasswordTransformer passwordTransformer, EmployeePublisher publisher) {
        this.repository = employeeRepository;
        this.mapper = jpaEmployeeMapper;
        this.transaction = transaction;
        this.propertyTransformer = propertyTransformer;
        this.passwordTransformer = passwordTransformer;
        this.publisher = publisher;
    }

    @Override
    public Result<Employee, Error> save(Employee employee) {
        var mapped = this.mapper.toJpa(employee);
        return this.propertyTransformer
                .addProperty(mapped.getAddress().getValue())
                .apply()
                .flatMap(v -> this.passwordTransformer.apply(mapped.getPassword()))
                .flatMap(v -> this.transaction
                        .write(() -> this.repository.save(mapped)))
                .flatMap(entity -> {
                    var domain = this.mapper.toDomain(entity);
                    return this.transaction
                            .triggers()
                            .add(publisher, domain)
                            .build()
                            .read(()-> this.repository.findById(entity.getId()));
                })
                .flatMap(op -> {
                    @SuppressWarnings("OptionalGetWithoutIsPresent")
                    var entity = op.get();
                    return this.propertyTransformer.addProperty(entity.getAddress().getValue())
                            .revert()
                            .map(v -> this.mapper.toDomain(entity));
                });
    }

    @Override
    public Result<Employee, Error> getEmployeeById(int id) {
        return transaction.
                read(()-> this.repository.findById(id))
                .flatMap(op ->
                    op.map(this.mapper::toDomain)
                            .map(Result::<Employee, Error>success)
                            .orElseGet(()-> Result.failure(
                                    ErrorType.USER_NOT_FOUND,
                                    new Field("employee", "No employee found with given id"))
                ));
    }


}
