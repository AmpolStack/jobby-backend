package com.jobby.business.infrastructure.persistence.country.jpa;

import com.jobby.business.domain.entities.Country;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JpaCountryMapper {
    Country toDomain(JpaCountryEntity jpaCountryEntity);
    JpaCountryEntity toJpa(Country country);
}
