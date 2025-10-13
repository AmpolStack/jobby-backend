package com.jobby.business.infrastructure.common;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import java.util.Optional;

public interface AfterPersistProcess<Infra, Domain> {
    Result<Domain, Error> use(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<Infra> infraOptional);
}
