package com.jobby.employee.infraestructure.persistence.mappers;

import com.jobby.employee.domain.model.Address;
import com.jobby.employee.infraestructure.persistence.entities.JpaAddressEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {JpaCityMapper.class})
public interface JpaAddressMapper {
    Address toDomain(JpaAddressEntity jpaAddressEntity);
}
