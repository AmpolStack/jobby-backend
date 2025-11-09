package com.jobby.business.feature.city.infrastructure.persistence.jpa;

import com.jobby.business.feature.city.domain.City;
import com.jobby.business.feature.country.infrastructure.persistence.jpa.JpaCountryMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { JpaCountryMapper.class})
public interface JpaCityMapper {
    City toDomain(JpaCityEntity jpaCityEntity);
    JpaCityEntity toJpa(City city);
}
