package com.jobby.employee.infraestructure.persistence.mappers;

import com.jobby.employee.domain.model.City;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { JpaCountryMapper.class})
public interface JpaCityMapper {
    City toDomain(JpaCityMapper jpaCityMapper);
}
