package com.jobby.infraestructure.common.repo;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.error.ErrorType;
import com.jobby.domain.mobility.error.Field;
import com.jobby.domain.mobility.result.Result;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class GenericRepository<Entity, Domain>{
    protected abstract Domain toDomain(Entity entity);
    protected abstract Entity toEntity(Domain domain);
    protected abstract Result<Entity, Error> modificationTransactionHandler(Function<Entity, Result<Entity, Error>> function, Entity entity);
    protected abstract Result<Optional<Entity>, Error> selectionTransactionHandler(Supplier<Optional<Entity>> supplier);

    protected Result<Domain, Error> select(Supplier<Optional<Entity>> supplier){
        return this.selectionTransactionHandler(supplier)
                .flatMap(optional ->
                    optional.map(this::toDomain)
                            .map(Result::<Domain, Error>success)
                            .orElseGet(() -> Result.failure(
                                    ErrorType.USER_NOT_FOUND,
                                    new Field("entity", "No entity found with given parameters")
                            ))
                );
    }

    protected Result<Domain, Error> select(Supplier<Optional<Entity>> supplier, Function<Entity, Result<Void, Error>> function){
        return this.selectionTransactionHandler(supplier)
                .flatMap(optional -> {

                    if(optional.isEmpty()){
                        return Result.failure(
                                ErrorType.USER_NOT_FOUND,
                                new Field("entity", "No entity found with given parameters"));
                    }

                    var value = optional.get();
                    return function.apply(value)
                        .flatMap(v -> {
                            var domain = this.toDomain(value);
                            return Result.success(domain);
                        });
                });
    }

    protected Result<Domain, Error> modify(Domain domain, Function<Entity, Result<Entity, Error>> function){
        var entity = toEntity(domain);
        return this.modificationTransactionHandler(function, entity)
                .map(this::toDomain);
    }
}
