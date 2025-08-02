package com.jobby.authorization.infraestructure.persistence.mappers;

import com.jobby.authorization.domain.model.Employee;
import com.jobby.authorization.infraestructure.persistence.entities.MongoEmployeeEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MongoEmployeeEntityMapper {
    Employee toDomain(MongoEmployeeEntity entity);
}
