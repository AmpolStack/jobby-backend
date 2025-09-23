package com.jobby.employee.infraestructure.persistence.mongo.mappers;

import com.jobby.employee.domain.model.City;
import com.jobby.employee.infraestructure.persistence.mongo.entities.MongoCityEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { MongoCountryMapper.class})
public interface MongoCityMapper {
    City toDomain(MongoCityEntity jpaCityEntity);
    MongoCityEntity toDocument(City city);
}
