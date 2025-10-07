package com.jobby.business.infrastructure.persistence.mongo.mappers;

import com.jobby.business.domain.entities.Country;
import com.jobby.business.infrastructure.persistence.mongo.entities.MongoCountryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MongoCountryMapper {
    Country toDomain(MongoCountryEntity mongoCountryMapper);
    MongoCountryEntity toDocument(Country country);
}
