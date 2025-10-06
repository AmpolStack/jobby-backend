package com.jobby.business.infrastructure.adapters.in.messaging.mappers;

import com.jobby.business.domain.Country;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CustomMappers.class})
public interface SchemaCountryMapper {
    com.jobby.messaging.schemas.Country toSchema(Country domainCountry);
    Country toDomain(com.jobby.messaging.schemas.Country schemaCountry);
}
