package com.jobby.business.infrastructure.persistence.city.jpa;

import com.jobby.business.domain.entities.City;
import com.jobby.business.infrastructure.persistence.country.jpa.JpaCountryMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { JpaCountryMapper.class})
public interface JpaCityMapper {
    City toDomain(JpaCityEntity jpaCityEntity);
    JpaCityEntity toJpa(City city);
}
