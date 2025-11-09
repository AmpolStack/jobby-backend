package com.jobby.business.feature.city.infrastructure.adapters.in.messaging;

import com.jobby.business.feature.business.infrastructure.adapters.in.messaging.mappers.CustomMappers;
import com.jobby.business.feature.country.infrastructure.adapter.in.messaging.SchemaCountryMapper;
import com.jobby.business.feature.city.domain.City;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { SchemaCountryMapper.class, CustomMappers.class})
public interface SchemaCityMapper {
    com.jobby.messaging.schemas.City toSchema(City domainCity);
    City toDomain(com.jobby.messaging.schemas.City schemaCity);
}
