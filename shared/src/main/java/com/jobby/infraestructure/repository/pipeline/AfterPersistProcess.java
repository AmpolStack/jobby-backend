package com.jobby.infraestructure.repository.pipeline;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.error.ErrorType;
import com.jobby.domain.mobility.error.Field;
import com.jobby.domain.mobility.result.Result;
import java.util.Optional;

public interface AfterPersistProcess<Infra, Domain> {
    default Result<Domain, Error> map(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<Infra> infra){
        return infra
                .map(this::map)
                .map(Result::<Domain, Error>success)
                .orElseGet(() -> Result.failure(ErrorType.USER_NOT_FOUND,
                        new Field(getEntityName(), "entity not foud")));
    }

    Domain map(Infra infra);
    String getEntityName();
    Result<Void, Error> mutate(Infra infra);
}
