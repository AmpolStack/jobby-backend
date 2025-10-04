package com.jobby.employee.infraestructure.persistence.mongo.mappers;

import com.jobby.employee.domain.model.Employee;
import com.jobby.employee.infraestructure.persistence.mongo.entities.MongoEmployeeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = { MongoAddressMapper.class, MongoSectionalMapper.class, MongoEmployeeStatusMapper.class, MongoUserMapper.class })
public interface MongoEmployeeMapper {
    Employee toDomain(MongoEmployeeEntity mongoEmployeeEntity);
    @Mapping(target = "usernameSearchable", ignore = true)
    @Mapping(target = "positionNameSearchable", ignore = true)
    MongoEmployeeEntity toDocument(Employee employee);

}
