package com.jobby.infraestructure.repository.pipeline;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;

public interface BeforePersistProcess<Infra, Domain> {
    Result<Infra, Error> before(Domain domain);
}
