package com.jobby.employee.infraestructure.persistence.jpa.mappers;

import com.jobby.employee.domain.model.Address;
import com.jobby.employee.infraestructure.persistence.jpa.entities.JpaAddressEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {JpaCityMapper.class})
public interface JpaAddressMapper {
    Address toDomain(JpaAddressEntity jpaAddressEntity);
    JpaAddressEntity toJpa(Address address);
}
