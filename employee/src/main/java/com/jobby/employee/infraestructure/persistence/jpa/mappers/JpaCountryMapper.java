package com.jobby.employee.infraestructure.persistence.jpa.mappers;

import com.jobby.employee.domain.model.Country;
import com.jobby.employee.infraestructure.persistence.jpa.entities.JpaCountryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JpaCountryMapper {
    Country toDomain(JpaCountryEntity jpaCountryEntity);
    JpaCountryEntity toJpa(Country country);
}
