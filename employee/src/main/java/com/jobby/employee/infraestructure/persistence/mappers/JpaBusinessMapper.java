package com.jobby.employee.infraestructure.persistence.mappers;

import com.jobby.employee.domain.model.Business;
import com.jobby.employee.infraestructure.persistence.entities.JpaBusinessEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { JpaBusinessMapper.class })
public interface JpaBusinessMapper {
    Business toDomain(JpaBusinessEntity jpaBusinessEntity);
}
