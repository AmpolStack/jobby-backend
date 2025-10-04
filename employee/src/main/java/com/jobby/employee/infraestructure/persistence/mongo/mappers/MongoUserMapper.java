package com.jobby.employee.infraestructure.persistence.mongo.mappers;

import com.jobby.employee.domain.model.User;
import com.jobby.employee.infraestructure.persistence.mongo.entities.MongoUserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MongoUserMapper {
    User toDomain(MongoUserEntity mongoUserEntity);
    @Mapping(target = "firstNameSearchable", ignore = true)
    @Mapping(target = "lastNameSearchable", ignore  = true)
    @Mapping(target = "emailSearchable", ignore = true)
    @Mapping(target = "phoneSearchable", ignore = true)
    MongoUserEntity toDocument(User user);
}
