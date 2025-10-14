package com.jobby.infraestructure.repository.pipeline;

public interface PipelinePersistenceProcess<Domain, Infra>
        extends AfterPersistProcess<Domain, Infra>,
        BeforePersistProcess<Domain, Infra> {
}
