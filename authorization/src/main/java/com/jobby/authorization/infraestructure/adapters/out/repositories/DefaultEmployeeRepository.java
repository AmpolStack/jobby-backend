package com.jobby.authorization.infraestructure.adapters.out.repositories;

import com.jobby.authorization.domain.model.Employee;
import com.jobby.authorization.domain.ports.out.repositories.EmployeeRepository;
import com.jobby.authorization.domain.result.Error;
import com.jobby.authorization.domain.result.ErrorType;
import com.jobby.authorization.domain.result.Field;
import com.jobby.authorization.domain.result.Result;
import com.jobby.authorization.infraestructure.persistence.mappers.MongoEmployeeEntityMapper;
import com.jobby.authorization.infraestructure.persistence.repositories.SpringDataMongoEmployeeRepository;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Service;

@Service
public class DefaultEmployeeRepository implements EmployeeRepository {

    private final MongoEmployeeEntityMapper mongoMapper;
    private final SpringDataMongoEmployeeRepository mongoRepository;

    public DefaultEmployeeRepository(MongoEmployeeEntityMapper mongoMapper, SpringDataMongoEmployeeRepository mongoRepository) {
        this.mongoMapper = mongoMapper;
        this.mongoRepository = mongoRepository;
    }

    @Override
    public Result<Employee, Error> findByEmailAndPassword(String email, String password) {
        try {
            return mongoRepository.findByEmailAndPassword(email, password)
                    .map(mongoMapper::toDomain)
                    .map(Result::<Employee, Error>success)
                    .orElseGet(() -> Result.failure(
                            ErrorType.USER_NOT_FOUND,
                            new Field("employee", "No employee found with given credentials")
                    ));
        } catch (DataAccessResourceFailureException ex) {
            return Result.failure(
                    ErrorType.ITN_EXTERNAL_SERVICE_FAILURE,
                    new Field("mongo.db.database", ex.getMessage())
            );
        }
    }

    @Override
    public Result<Employee, Error> findById(int id) {
        try {
            return mongoRepository.findById(id)
                    .map(mongoMapper::toDomain)
                    .map(Result::<Employee, Error>success)
                    .orElseGet(() -> Result.failure(
                            ErrorType.USER_NOT_FOUND,
                            new Field("employee", "No employee found with given credentials")
                    ));
        } catch (DataAccessResourceFailureException ex) {
            return Result.failure(
                    ErrorType.ITN_EXTERNAL_SERVICE_FAILURE,
                    new Field("mongo.db.database", ex.getMessage())
            );
        }
    }

}
