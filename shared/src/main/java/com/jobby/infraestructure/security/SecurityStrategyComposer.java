package com.jobby.infraestructure.security;

public interface SecurityStrategyComposer<Entity> extends
    SecurityStrategyImplementer<Entity>,
    SecurityStrategyReverter<Entity> {
}
