package com.jobby.infraestructure.common.repo;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.error.ErrorType;
import com.jobby.domain.mobility.error.Field;
import com.jobby.domain.mobility.result.Result;
import org.springframework.dao.*;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class JpaGenericRepository<Entity, Domain> extends GenericRepository<Entity, Domain> {

    @Override
    protected Result<Entity, Error> modificationTransactionHandler(Function<Entity, Result<Entity, Error>> function, Entity entity) {
        Result<Entity, Error> response;

        try {
            response = function.apply(entity);
        } catch (DuplicateKeyException ex) {
            response = Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("unique constraint", "Duplicate key or unique constraint violation"));
        } catch (DataIntegrityViolationException ex) {
            response = Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("constraint violation", "Referential integrity or constraint violation"));
        } catch (OptimisticLockingFailureException ex) {
            response = Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("optimistic lock", "Optimistic locking failure: record was modified by another transaction"));
        } catch (CannotAcquireLockException ex) {
            response = Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("deadlock", "Transaction deadlock detected while acquiring lock"));
        } catch (PessimisticLockingFailureException ex) {
            response = Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("pessimistic lock", "Pessimistic locking failure: could not acquire lock on the record"));
        } catch (QueryTimeoutException ex) {
            response = Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("timeout", "Query or transaction execution time exceeded the allowed limit"));
        } catch (DataAccessResourceFailureException ex) {
            response = Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("database access", "Database connection or resource failure"));
        } catch (TransientDataAccessException ex) {
            response = Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("transient error", "Temporary data access error occurred, please retry"));
        } catch (NonTransientDataAccessException ex) {
            response = Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("persistent error", "Permanent data access error occurred, cannot recover automatically"));
        } catch (DataAccessException ex) {
            response = Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("generic database", "Unclassified database access error"));
        } catch (Exception ex) {
            response = Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("unexpected", "Unexpected error: " + ex.getMessage()));
        }

        return response;
    }

    @Override
    protected Result<Optional<Entity>, Error> selectionTransactionHandler(Supplier<Optional<Entity>> supplier) {
        Result<Optional<Entity>, Error> response;

        try {
            Optional<Entity> result = supplier.get();
            response = Result.success(result);

        } catch (EmptyResultDataAccessException ex) {
            response = Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("not found", "No entity was found matching the given criteria"));
        } catch (IncorrectResultSizeDataAccessException ex) {
            response = Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("result size", "Multiple results found when only one was expected"));
        } catch (QueryTimeoutException ex) {
            response = Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("timeout", "Query execution exceeded the allowed time limit"));
        } catch (DataAccessResourceFailureException ex) {
            response = Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("database access", "Database connection or resource failure"));
        } catch (DataRetrievalFailureException ex) {
            response = Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("data retrieval", "Error retrieving data from the database"));
        } catch (TransientDataAccessException ex) {
            response = Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("transient error", "Temporary issue occurred while reading data, please retry"));
        } catch (NonTransientDataAccessException ex) {
            response = Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("persistent error", "Permanent data access issue encountered during read operation"));
        } catch (DataAccessException ex) {
            response = Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("generic database", "Unclassified database access error"));
        } catch (Exception ex) {
            response = Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("unexpected", "Unexpected error: " + ex.getMessage()));
        }

        return response;
    }
}
