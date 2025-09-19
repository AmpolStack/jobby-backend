package com.jobby.employee.infraestructure.persistence.mappers;

import com.jobby.employee.domain.model.Sectional;
import com.jobby.employee.infraestructure.persistence.entities.JpaSectionalEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { JpaSectionalMapper.class, JpaBusinessMapper.class })
public interface JpaSectionalMapper {
    Sectional toDomain(JpaSectionalEntity jpaSectionalEntity);
}
