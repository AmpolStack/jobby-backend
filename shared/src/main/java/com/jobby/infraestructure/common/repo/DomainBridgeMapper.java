package com.jobby.infraestructure.common.repo;

public interface DomainBridgeMapper<Infra, Domain> {
    Domain toDomain(Infra infraEntity);
    Infra toEntity(Domain domainEntity);
}