package com.jobby.business.infrastructure.persistence.mongo.error;

import com.jobby.infraestructure.repository.error.PersistenceErrorHandler;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.error.ErrorType;
import com.jobby.domain.mobility.error.Field;
import com.jobby.domain.mobility.result.Result;
import org.springframework.dao.*;
import org.springframework.data.mongodb.MongoTransactionException;
import org.springframework.stereotype.Component;
import java.util.function.Function;
import java.util.function.Supplier;

@Component("MongoPersistenceErrorHandler")
public class MongoPersistenceErrorHandler implements PersistenceErrorHandler {
    @Override
    public <Entity> Result<Integer, Error> handleWriting(Function<Entity, Integer> function, Entity entity) {
        try {
            var applied = function.apply(entity);
            return Result.success(applied);
        } catch (DuplicateKeyException ex) {
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("duplicate key", "Duplicate key or unique constraint violation"));
        } catch (DataIntegrityViolationException ex) {
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("data integrity", "Data integrity or constraint violation"));
        } catch (OptimisticLockingFailureException ex) {
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("optimistic lock", "Optimistic locking failure: document modified by another transaction"));
        } catch (MongoTransactionException ex) {
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("transaction", "Transaction failed or aborted unexpectedly"));
        } catch (RecoverableDataAccessException ex) {
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("resource", "MongoDB resource unavailable or network failure"));
        } catch (TransientDataAccessException ex) {
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("transient", "Transient MongoDB access issue, please retry"));
        } catch (NonTransientDataAccessException ex) {
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("non-transient", "Persistent MongoDB data access issue"));
        } catch (DataAccessException ex) {
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("data access", "Generic MongoDB data access error"));
        } catch (Exception ex) {
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("unexpected", "Unexpected error: " + ex.getMessage()));
        }
    }

    @Override
    public <Entity> Result<Entity, Error> handleReading(Supplier<Entity> supplier) {
        try {
            var result = supplier.get();
            return Result.success(result);
        } catch (QueryTimeoutException ex) {
           return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("timeout", "Query execution exceeded maximum allowed time"));
        } catch (TransientDataAccessException ex) {
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("transient", "Transient MongoDB access issue, please retry"));
        } catch (NonTransientDataAccessException ex) {
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("non-transient", "Non-transient MongoDB data access issue"));
        } catch (DataAccessException ex) {
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("data access", "Generic MongoDB data access error"));
        } catch (Exception ex) {
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("unexpected", "Unexpected error: " + ex.getMessage()));
        }
    }
}
