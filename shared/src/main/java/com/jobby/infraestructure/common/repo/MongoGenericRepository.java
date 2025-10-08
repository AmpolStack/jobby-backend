package com.jobby.infraestructure.common.repo;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.error.ErrorType;
import com.jobby.domain.mobility.error.Field;
import com.jobby.domain.mobility.result.Result;
import org.springframework.dao.*;
import org.springframework.data.mongodb.MongoTransactionException;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class MongoGenericRepository<Entity,Domain> extends GenericRepository<Entity, Domain> {

    @Override
    protected Result<Entity, Error> modificationTransactionHandler(Function<Entity, Result<Entity, Error>> function, Entity entity) {
        Result<Entity, Error> response;

        try {
            response = function.apply(entity);
        } catch (DuplicateKeyException ex) {
            response = Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("duplicate key", "Duplicate key or unique constraint violation"));
        } catch (DataIntegrityViolationException ex) {
            response = Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("data integrity", "Data integrity or constraint violation"));
        } catch (OptimisticLockingFailureException ex) {
            response = Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("optimistic lock", "Optimistic locking failure: document modified by another transaction"));
        } catch (MongoTransactionException ex) {
            response = Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("transaction", "Transaction failed or aborted unexpectedly"));
        } catch (RecoverableDataAccessException ex) {
            response = Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("resource", "MongoDB resource unavailable or network failure"));
        } catch (TransientDataAccessException ex) {
            response = Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("transient", "Transient MongoDB access issue, please retry"));
        } catch (NonTransientDataAccessException ex) {
            response = Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("non-transient", "Persistent MongoDB data access issue"));
        } catch (DataAccessException ex) {
            response = Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("data access", "Generic MongoDB data access error"));
        } catch (Exception ex) {
            response = Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("unexpected", "Unexpected error: " + ex.getMessage()));
        }

        return response;
    }

    @Override
    public Result<Optional<Entity>, Error> selectionTransactionHandler(Supplier<Optional<Entity>> supplier) {
        Result<Optional<Entity>, Error> response;

        try {
            Optional<Entity> result = supplier.get();
            response = Result.success(result);

        } catch (QueryTimeoutException ex) {
            response = Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("timeout", "Query execution exceeded maximum allowed time"));
        } catch (TransientDataAccessException ex) {
            response = Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("transient", "Transient MongoDB access issue, please retry"));
        } catch (NonTransientDataAccessException ex) {
            response = Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("non-transient", "Non-transient MongoDB data access issue"));
        } catch (DataAccessException ex) {
            response = Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("data access", "Generic MongoDB data access error"));
        } catch (Exception ex) {
            response = Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("unexpected", "Unexpected error: " + ex.getMessage()));
        }

        return response;
    }
}
