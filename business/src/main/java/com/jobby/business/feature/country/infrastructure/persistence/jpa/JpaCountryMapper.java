package com.jobby.business.feature.country.infrastructure.persistence.jpa;

import com.jobby.business.feature.country.domain.Country;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JpaCountryMapper {
    Country toDomain(JpaCountryEntity jpaCountryEntity);
    JpaCountryEntity toJpa(Country country);
}
