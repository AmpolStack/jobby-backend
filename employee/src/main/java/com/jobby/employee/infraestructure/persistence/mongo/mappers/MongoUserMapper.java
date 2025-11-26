package com.jobby.employee.infraestructure.persistence.mongo.mappers;

import com.jobby.employee.domain.model.User;
import com.jobby.employee.infraestructure.persistence.mongo.entities.MongoUserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MongoUserMapper {

    // Leemos directamente los campos planos (cifrados) de Mongo
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "firstName", source = "firstName")
    User toDomain(MongoUserEntity mongoUserEntity);

    // Escribimos el valor lógico directamente en los campos planos
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "firstName", source = "firstName")
    MongoUserEntity toDocument(User user);
}
