package com.jobby.business.infrastructure.persistence.city.mongo;

import com.jobby.business.domain.entities.City;
import com.jobby.business.infrastructure.persistence.country.mongo.MongoCountryMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { MongoCountryMapper.class})
public interface MongoCityMapper {
    City toDomain(MongoCityEntity jpaCityEntity);
    MongoCityEntity toDocument(City city);
}
