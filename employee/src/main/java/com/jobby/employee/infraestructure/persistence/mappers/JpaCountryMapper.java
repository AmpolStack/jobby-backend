package com.jobby.employee.infraestructure.persistence.mappers;

import com.jobby.employee.domain.model.Country;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JpaCountryMapper {
    Country toDomain(JpaCountryMapper jpaCountryMapper);
}
