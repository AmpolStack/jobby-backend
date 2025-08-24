package com.jobby.employee.infraestructure.persistence.mappers;

import com.jobby.employee.domain.model.Employee;
import com.jobby.employee.infraestructure.persistence.entities.JpaEmployeeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { JpaAddressMapper.class, JpaSectionalMapper.class, JpaEmployeeStatusMapper.class })
public interface JpaEmployeeMapper {

    @Mapping(target = "user", ignore = true)
    Employee toDomain(JpaEmployeeEntity jpaEmployeeEntity);
}
