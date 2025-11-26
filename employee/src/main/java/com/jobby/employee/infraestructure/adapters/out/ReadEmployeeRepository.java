package com.jobby.employee.infraestructure.adapters.out;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.error.ErrorType;
import com.jobby.domain.mobility.error.Field;
import com.jobby.domain.mobility.result.Result;
import com.jobby.domain.ports.hashing.HashingService;
import com.jobby.employee.domain.model.Employee;
import com.jobby.employee.domain.ports.out.EmployeeRepository;
import com.jobby.employee.infraestructure.persistence.mongo.entities.MongoEmployeeEntity;
import com.jobby.employee.infraestructure.persistence.mongo.mappers.MongoEmployeeMapper;
import com.jobby.employee.infraestructure.persistence.mongo.repositories.SpringDataMongoEmployeeRepository;
import com.jobby.infraestructure.security.SecuredPasswordTransformer;
import com.jobby.infraestructure.security.SecuredPropertyComposer;
import com.jobby.infraestructure.security.SecuredPropertyTransformer;
import com.jobby.infraestructure.transaction.orchetration.TransactionalOrchestrator;
import org.springframework.stereotype.Component;

@Component("readEmployeeRepository")
public class ReadEmployeeRepository implements EmployeeRepository {

    private final SpringDataMongoEmployeeRepository repository;
    private final MongoEmployeeMapper mapper;
    private final TransactionalOrchestrator transaction;
    private final SecuredPropertyComposer securedComposer;
    private final HashingService hashingService;


    public ReadEmployeeRepository(SpringDataMongoEmployeeRepository employeeRepository, MongoEmployeeMapper mongoEmployeeMapper, TransactionalOrchestrator transaction, SecuredPropertyComposer securedComposer, HashingService hashingService) {
        this.repository = employeeRepository;
        this.mapper = mongoEmployeeMapper;
        this.transaction = transaction;
        this.securedComposer = securedComposer;
        this.hashingService = hashingService;
    }

    @Override
    public Result<Employee, Error> save(Employee employee) {
        var mapped = this.mapper.toDocument(employee);
        return this.securedComposer
                .apply(mapped.getUsername(), (cipher, mac) -> {
                    mapped.setUsername(cipher);
                    mapped.setUsernameSearchable(mac);
                })
                .apply(mapped.getPositionName(), (cipher, mac) -> {
                    mapped.setPositionName(cipher);
                    mapped.setPositionNameSearchable(mac);
                })
                .apply(mapped.getUsername(), (cipher, mac) -> {
                    mapped.setUsername(cipher);
                    mapped.setUsernameSearchable(mac);
                })
                .apply(mapped.getUser().getLastName(), (cipher, mac) -> {
                    mapped.getUser().setLastName(cipher);
                    mapped.getUser().setLastNameSearchable(mac);
                })
                .apply(mapped.getUser().getFirstName(), (cipher, mac) -> {
                    mapped.getUser().setFirstName(cipher);
                    mapped.getUser().setFirstNameSearchable(mac);
                })
                .apply(mapped.getUser().getEmail(), (cipher, mac) -> {
                    mapped.getUser().setEmail(cipher);
                    mapped.getUser().setEmailSearchable(mac);
                })
                .apply(mapped.getUser().getPhone(), (cipher, mac) -> {
                    mapped.getUser().setPhone(cipher);
                    mapped.getUser().setPhoneSearchable(mac);
                })
                .apply(mapped.getAddress().getValue(), (cipher, mac)->{
                    mapped.getAddress().setValue(cipher);
                    mapped.getAddress().setValueSearchable(mac);
                })
                .build()
                .flatMap(v -> this.hashingService.hash(mapped.getPassword()))
                .flatMap(hash -> {
                    mapped.setPassword(hash);
                    return this.transaction
                            .write(() -> this.repository.save(mapped));
                })
                .map(entity -> null);
    }

    @Override
    public Result<Employee, Error> getEmployeeById(int id) {
        return transaction.
                read(()-> this.repository.findById(id))
                .flatMap(op ->{
                        var com = op;
                        return op
                                .map(Result::<MongoEmployeeEntity, Error>success)
                                .orElseGet(()-> Result.failure(
                                        ErrorType.USER_NOT_FOUND,
                                        new Field("employee", "No employee found with given id"))
                                );
                })
                .flatMap(entity -> this.securedComposer
                        .revert(entity.getUsername(), entity::setUsername)
                        .revert(entity.getPositionName(), entity::setPositionName)
                        .revert(entity.getUsername(), entity::setUsername)
                        .revert(entity.getUser().getLastName(), (v) -> entity.getUser().setLastName(v))
                        .revert(entity.getUser().getFirstName(), (v) -> entity.getUser().setFirstName(v))
                        .revert(entity.getUser().getEmail(), (v) -> entity.getUser().setEmail(v))
                        .revert(entity.getUser().getPhone(), (v) -> entity.getUser().setPhone(v))
                        .revert(entity.getAddress().getValue(),(v) -> entity.getAddress().setValue(v))
                        .build()
                        .map(v -> this.mapper.toDomain(entity)));
    }

}
