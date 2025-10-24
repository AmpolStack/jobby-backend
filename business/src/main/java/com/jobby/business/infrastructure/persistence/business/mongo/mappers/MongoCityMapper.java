package com.jobby.business.infrastructure.persistence.business.mongo.mappers;

import com.jobby.business.domain.entities.City;
import com.jobby.business.infrastructure.persistence.business.mongo.entities.MongoCityEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { MongoCountryMapper.class})
public interface MongoCityMapper {
    City toDomain(MongoCityEntity jpaCityEntity);
    MongoCityEntity toDocument(City city);
}
