package com.jobby.infraestructure.repository.pipeline;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;

import java.util.Optional;

public interface BeforePersistProcess<Infra, Domain> {
    Infra map(Domain domain);
    Result<Void, Error> mutate(Infra infra);
}
