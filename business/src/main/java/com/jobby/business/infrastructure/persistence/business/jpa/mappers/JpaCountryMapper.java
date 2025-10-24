package com.jobby.business.infrastructure.persistence.business.jpa.mappers;

import com.jobby.business.domain.entities.Country;
import com.jobby.business.infrastructure.persistence.business.jpa.entities.JpaCountryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JpaCountryMapper {
    Country toDomain(JpaCountryEntity jpaCountryEntity);
    JpaCountryEntity toJpa(Country country);
}
