package com.jobby.employee.infraestructure.persistence.mongo.mappers;

import com.jobby.employee.domain.model.City;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { MongoCountryMapper.class})
public interface MongoCityMapper {
    City toDomain(MongoCityMapper jpaCityMapper);
}
