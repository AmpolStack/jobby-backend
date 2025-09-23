package com.jobby.employee.infraestructure.adapters.in.messaging.mappers;

import com.jobby.employee.domain.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CustomMappers.class})
public interface SchemaUserMapper {
    com.jobby.messaging.schemas.User toSchema(User domainUser);
    User toDomain(com.jobby.messaging.schemas.User schemaUser);
}
