package com.jobby.employee.infraestructure.adapters.in.messaging.mappers;

import com.jobby.employee.domain.model.City;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { SchemaCountryMapper.class, CustomMappers.class})
public interface SchemaCityMapper {
    com.jobby.messaging.schemas.City toSchema(City domainCity);
    City toDomain(com.jobby.messaging.schemas.City schemaCity);
}
