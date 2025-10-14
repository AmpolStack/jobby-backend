package com.jobby.infraestructure.security;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;

public interface SecurityStrategyImplementer<Entity> {
    Result<Void, Error> apply(Entity entity);
}
