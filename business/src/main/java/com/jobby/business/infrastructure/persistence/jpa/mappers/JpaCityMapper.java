package com.jobby.business.infrastructure.persistence.jpa.mappers;

import com.jobby.business.domain.entities.City;
import com.jobby.business.infrastructure.persistence.jpa.entities.JpaCityEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { JpaCountryMapper.class})
public interface JpaCityMapper {
    City toDomain(JpaCityEntity jpaCityEntity);
    JpaCityEntity toJpa(City city);
}
