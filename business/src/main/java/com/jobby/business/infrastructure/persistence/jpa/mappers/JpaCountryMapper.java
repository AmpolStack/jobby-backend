package com.jobby.business.infrastructure.persistence.jpa.mappers;

import com.jobby.business.domain.Country;
import com.jobby.business.infrastructure.persistence.jpa.entities.JpaCountryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JpaCountryMapper {
    Country toDomain(JpaCountryEntity jpaCountryEntity);
    JpaCountryEntity toJpa(Country country);
}
