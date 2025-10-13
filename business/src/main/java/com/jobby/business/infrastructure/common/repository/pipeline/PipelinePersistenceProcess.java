package com.jobby.business.infrastructure.common.repository.pipeline;

public interface PipelinePersistenceProcess<Domain, Infra>
        extends AfterPersistProcess<Domain, Infra>,
        BeforePersistProcess<Domain, Infra> {
}
