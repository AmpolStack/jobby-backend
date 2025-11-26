package com.jobby.business.feature.country.infrastructure.adapter.in.messaging;

import com.jobby.business.feature.business.infrastructure.adapters.in.messaging.mappers.CustomMappers;
import com.jobby.business.feature.country.infrastructure.persistence.jpa.JpaCountryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CustomMappers.class})
public interface SchemaCountryMapper {
    com.jobby.messaging.schemas.Country toSchema(JpaCountryEntity jpaCountryEntity);
    JpaCountryEntity toEntity(com.jobby.messaging.schemas.Country schemaCountry);
}
