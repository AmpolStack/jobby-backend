package com.jobby.business.infrastructure.persistence.country.mongo;

import com.jobby.business.domain.entities.Country;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MongoCountryMapper {
    Country toDomain(MongoCountryEntity mongoCountryMapper);
    MongoCountryEntity toDocument(Country country);
}
