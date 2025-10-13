package com.jobby.infraestructure.repository.pipeline;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import java.util.Optional;

public interface AfterPersistProcess<Infra, Domain> {
    Result<Domain, Error> after(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<Infra> infraOptional);
}
