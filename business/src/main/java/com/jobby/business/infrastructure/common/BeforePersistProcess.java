package com.jobby.business.infrastructure.common;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;

public interface BeforePersistProcess<Domain, Infra> {
    Result<Infra, Error> use(Domain infra);
}
