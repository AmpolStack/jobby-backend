package com.jobby.business.infrastructure.common;

public interface PipelinePersistenceProcess<Domain, Infra>
        extends AfterPersistProcess<Domain, Infra>,
        BeforePersistProcess<Domain, Infra> {
}
