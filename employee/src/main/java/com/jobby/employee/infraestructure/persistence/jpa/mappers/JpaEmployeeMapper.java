package com.jobby.employee.infraestructure.persistence.jpa.mappers;

import com.jobby.employee.domain.model.Employee;
import com.jobby.employee.infraestructure.persistence.jpa.entities.JpaEmployeeEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        uses = { JpaAddressMapper.class, JpaSectionalMapper.class, JpaEmployeeStatusMapper.class, JpaUserMapper.class })
public interface JpaEmployeeMapper {

    Employee toDomain(JpaEmployeeEntity jpaEmployeeEntity);

}
