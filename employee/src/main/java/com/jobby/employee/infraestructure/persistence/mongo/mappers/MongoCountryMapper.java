package com.jobby.employee.infraestructure.persistence.mongo.mappers;

import com.jobby.employee.domain.model.Country;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MongoCountryMapper {
    Country toDomain(MongoCountryMapper mongoCountryMapper);
}
