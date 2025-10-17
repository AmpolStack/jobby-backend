package com.jobby.infraestructure.repository.pipeline;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.error.ErrorType;
import com.jobby.domain.mobility.error.Field;
import com.jobby.domain.mobility.result.Result;
import java.util.Optional;

public interface AfterPersistProcess<Infra, Domain> {
    default Result<Infra, Error> exist(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<Infra> infra){
        return infra
                .map(Result::<Infra, Error>success)
                .orElseGet(() -> Result.failure(ErrorType.USER_NOT_FOUND,
                        new Field(this.getEntityName(), "entity not foud")));
    }

    default Result<Void, Error> exist(Boolean isFind){
        if(!isFind){
            return Result.failure(ErrorType.USER_NOT_FOUND,
                    new Field(this.getEntityName(), "entity not foud"));
        }

        return Result.success(null);
    }

    Domain map(Infra infra);
    String getEntityName();
    Result<Void, Error> mutate(Infra infra);
}
