package com.jobby.business.infrastructure.common.errorHandling;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.error.ErrorType;
import com.jobby.domain.mobility.error.Field;
import com.jobby.domain.mobility.result.Result;
import org.springframework.dao.*;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@Component("JpaPersistenceErrorHandler")
public class JpaPersistenceErrorHandler implements PersistenceErrorHandler {
    @Override
    public <Entity> Result<Optional<Entity>, Error> handleWriting(Function<Entity, Optional<Entity>> function, Entity entity) {
        try {
            var applied = function.apply(entity);
            return Result.success(applied);
        } catch (DuplicateKeyException ex) {
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("unique constraint", "Duplicate key or unique constraint violation"));
        } catch (DataIntegrityViolationException ex) {
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("constraint violation", "Referential integrity or constraint violation"));
        } catch (OptimisticLockingFailureException ex) {
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("optimistic lock", "Optimistic locking failure: record was modified by another transaction"));
        } catch (CannotAcquireLockException ex) {
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("deadlock", "Transaction deadlock detected while acquiring lock"));
        } catch (PessimisticLockingFailureException ex) {
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("pessimistic lock", "Pessimistic locking failure: could not acquire lock on the record"));
        } catch (QueryTimeoutException ex) {
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("timeout", "Query or transaction execution time exceeded the allowed limit"));
        } catch (DataAccessResourceFailureException ex) {
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("database access", "Database connection or resource failure"));
        } catch (TransientDataAccessException ex) {
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("transient error", "Temporary data access error occurred, please retry"));
        } catch (NonTransientDataAccessException ex) {
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("persistent error", "Permanent data access error occurred, cannot recover automatically"));
        } catch (DataAccessException ex) {
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("generic database", "Unclassified database access error"));
        } catch (Exception ex) {
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("unexpected", "Unexpected error: " + ex.getMessage()));
        }
    }

    @Override
    public <Entity> Result<Optional<Entity>, Error> handleReading(Supplier<Optional<Entity>> supplier) {
        try {
            Optional<Entity> result = supplier.get();
            return Result.success(result);

        } catch (EmptyResultDataAccessException ex) {
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("not found", "No entity was found matching the given criteria"));
        } catch (IncorrectResultSizeDataAccessException ex) {
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("result size", "Multiple results found when only one was expected"));
        } catch (QueryTimeoutException ex) {
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("timeout", "Query execution exceeded the allowed time limit"));
        } catch (DataAccessResourceFailureException ex) {
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("database access", "Database connection or resource failure"));
        } catch (DataRetrievalFailureException ex) {
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("data retrieval", "Error retrieving data from the database"));
        } catch (TransientDataAccessException ex) {
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("transient error", "Temporary issue occurred while reading data, please retry"));
        } catch (NonTransientDataAccessException ex) {
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("persistent error", "Permanent data access issue encountered during read operation"));
        } catch (DataAccessException ex) {
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("generic database", "Unclassified database access error"));
        } catch (Exception ex) {
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("unexpected", "Unexpected error: " + ex.getMessage()));
        }
    }


}
