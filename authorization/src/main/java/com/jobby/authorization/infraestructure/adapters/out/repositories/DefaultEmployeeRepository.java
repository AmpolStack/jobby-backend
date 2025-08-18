package com.jobby.authorization.infraestructure.adapters.out.repositories;

import com.jobby.authorization.domain.model.Employee;
import com.jobby.authorization.domain.ports.out.repositories.EmployeeRepository;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.error.ErrorType;
import com.jobby.domain.mobility.error.Field;
import com.jobby.domain.mobility.result.Result;
import com.jobby.authorization.domain.shared.validators.ValidationChain;
import com.jobby.authorization.infraestructure.persistence.entities.MongoEmployeeEntity;
import com.jobby.authorization.infraestructure.persistence.mappers.MongoEmployeeEntityMapper;
import com.jobby.authorization.infraestructure.persistence.repositories.SpringDataMongoEmployeeRepository;
import io.jsonwebtoken.lang.Supplier;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class DefaultEmployeeRepository implements EmployeeRepository {

    private final MongoEmployeeEntityMapper mongoMapper;
    private final SpringDataMongoEmployeeRepository mongoRepository;

    public DefaultEmployeeRepository(MongoEmployeeEntityMapper mongoMapper, SpringDataMongoEmployeeRepository mongoRepository) {
        this.mongoMapper = mongoMapper;
        this.mongoRepository = mongoRepository;
    }

    private Result<Employee, Error> genericExecution(Supplier<Optional<MongoEmployeeEntity>> supplier, String failureInstanceName) {
        try{
            return supplier
                    .get()
                    .map(mongoMapper::toDomain)
                    .map(Result::<Employee, Error>success)
                    .orElseGet(() ->
                        Result.failure(
                                ErrorType.USER_NOT_FOUND,
                                new Field("employee", "No employee found with given " + failureInstanceName)
                        )
                    );
        }
        catch (DataAccessResourceFailureException ex){
            return Result.failure(
                    ErrorType.ITS_EXTERNAL_SERVICE_FAILURE,
                    new Field("mongo.db.database", ex.getMessage())
            );
        }
    }


    @Override
    public Result<Employee, Error> findByEmailAndPassword(String email, String password) {
        return ValidationChain.create()
                .validateInternalNotBlank(email, "email")
                .validateInternalNotBlank(password, "password")
                .build()
                .flatMap(x -> genericExecution(() -> this.mongoRepository.findByEmailAndPassword(email, password), "credentials"));

    }

    @Override
    public Result<Employee, Error> findById(int id) {
        return ValidationChain.create()
                .validateInternalGreaterThan(id, 0, "employee-id")
                .build()
                .flatMap(v -> genericExecution(() -> this.mongoRepository.findById(id), "id"));
    }

}
