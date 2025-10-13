package com.jobby.business.infrastructure.secure;

public interface SecurityStrategyComposer<Entity> extends
    SecurityStrategyImplementer<Entity>,
    SecurityStrategyReverter<Entity> {
}
