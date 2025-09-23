package com.jobby.employee.infraestructure.persistence.mongo.mappers;

import com.jobby.employee.domain.model.User;
import com.jobby.employee.infraestructure.persistence.mongo.entities.MongoUserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MongoUserMapper {
    User toDomain(MongoUserEntity mongoUserEntity);
    MongoUserEntity toDocument(User user);
}
