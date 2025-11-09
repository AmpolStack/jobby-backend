package com.jobby.business.feature.country.infrastructure.persistence.mongo;

import com.jobby.business.feature.country.domain.Country;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MongoCountryMapper {
    Country toDomain(MongoCountryEntity mongoCountryMapper);
    MongoCountryEntity toDocument(Country country);
}
