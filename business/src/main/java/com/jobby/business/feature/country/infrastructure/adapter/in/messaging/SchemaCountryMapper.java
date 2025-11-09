package com.jobby.business.feature.country.infrastructure.adapter.in.messaging;

import com.jobby.business.feature.business.infrastructure.adapters.in.messaging.mappers.CustomMappers;
import com.jobby.business.feature.country.domain.Country;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CustomMappers.class})
public interface SchemaCountryMapper {
    com.jobby.messaging.schemas.Country toSchema(Country domainCountry);
    Country toDomain(com.jobby.messaging.schemas.Country schemaCountry);
}
