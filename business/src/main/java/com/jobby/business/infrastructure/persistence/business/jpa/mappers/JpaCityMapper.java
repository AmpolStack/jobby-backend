package com.jobby.business.infrastructure.persistence.business.jpa.mappers;

import com.jobby.business.domain.entities.City;
import com.jobby.business.infrastructure.persistence.business.jpa.entities.JpaCityEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { JpaCountryMapper.class})
public interface JpaCityMapper {
    City toDomain(JpaCityEntity jpaCityEntity);
    JpaCityEntity toJpa(City city);
}
