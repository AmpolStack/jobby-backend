package com.jobby.employee.infraestructure.persistence.mongo.mappers;

import com.jobby.employee.domain.model.EmployeeStatus;
import com.jobby.employee.infraestructure.persistence.mongo.entities.MongoEmployeeStatusEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MongoEmployeeStatusMapper {
    EmployeeStatus toDomain(MongoEmployeeStatusEntity mongoEmployeeStatusEntity);
}
