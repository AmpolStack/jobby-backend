package com.jobby.employee.infraestructure.persistence.mappers;

import com.jobby.employee.domain.model.Employee;
import com.jobby.employee.infraestructure.persistence.entities.JpaEmployeeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JpaEmployeeMapper {
    
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "sectional", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "positionName", source = "positionName")
    @Mapping(target = "profileImageUrl", source = "profileImageUrl")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "modifiedAt", source = "modifiedAt")
    Employee toDomain(JpaEmployeeEntity jpaEmployeeEntity);
}
