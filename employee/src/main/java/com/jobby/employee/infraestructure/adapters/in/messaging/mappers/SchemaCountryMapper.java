package com.jobby.employee.infraestructure.adapters.in.messaging.mappers;

import com.jobby.employee.domain.model.Country;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CustomMappers.class})
public interface SchemaCountryMapper {
    com.jobby.messaging.schemas.Country toSchema(Country domainCountry);
    Country toDomain(com.jobby.messaging.schemas.Country schemaCountry);
}
