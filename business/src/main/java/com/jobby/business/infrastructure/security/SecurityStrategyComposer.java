package com.jobby.business.infrastructure.security;

public interface SecurityStrategyComposer<Entity> extends
    SecurityStrategyImplementer<Entity>,
    SecurityStrategyReverter<Entity> {
}
