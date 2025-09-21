package com.jobby.employee.infraestructure.persistence.jpa.mappers;

import com.jobby.employee.domain.model.City;
import com.jobby.employee.infraestructure.persistence.jpa.entities.JpaCityEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { JpaCountryMapper.class})
public interface JpaCityMapper {
    City toDomain(JpaCityEntity jpaCityEntity);
    JpaCityEntity toJpa(City city);
}
