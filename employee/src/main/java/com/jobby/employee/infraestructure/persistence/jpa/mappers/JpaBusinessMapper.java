package com.jobby.employee.infraestructure.persistence.jpa.mappers;

import com.jobby.employee.domain.model.Business;
import com.jobby.employee.infraestructure.persistence.jpa.entities.JpaBusinessEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { JpaAddressMapper.class })
public interface JpaBusinessMapper {
    Business toDomain(JpaBusinessEntity jpaBusinessEntity);
    JpaBusinessEntity toJpa(Business business);
}
