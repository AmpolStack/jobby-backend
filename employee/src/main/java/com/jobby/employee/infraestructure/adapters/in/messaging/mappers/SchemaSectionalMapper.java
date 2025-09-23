package com.jobby.employee.infraestructure.adapters.in.messaging.mappers;

import com.jobby.employee.domain.model.Sectional;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { SchemaSectionalMapper.class, SchemaBusinessMapper.class, CustomMappers.class })
public interface SchemaSectionalMapper {
    com.jobby.messaging.schemas.Sectional toSchema(Sectional domainSectional);
    Sectional toDomain(com.jobby.messaging.schemas.Sectional schemaSectional);
}
