package com.jobby.business.infrastructure.security;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;

public interface SecurityStrategyReverter<Entity> {
    Result<Void, Error> revert(Entity entity);
}
