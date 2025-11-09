package com.jobby.business.feature.city.infrastructure.persistence.mongo;

import com.jobby.business.feature.city.domain.City;
import com.jobby.business.feature.country.infrastructure.persistence.mongo.MongoCountryMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { MongoCountryMapper.class})
public interface MongoCityMapper {
    City toDomain(MongoCityEntity jpaCityEntity);
    MongoCityEntity toDocument(City city);
}
