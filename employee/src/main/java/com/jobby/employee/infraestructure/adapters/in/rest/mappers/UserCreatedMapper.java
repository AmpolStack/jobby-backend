package com.jobby.employee.infraestructure.adapters.in.rest.controllers.dtos.mappers;

import com.jobby.employee.domain.model.User;
import com.jobby.employee.infraestructure.adapters.in.rest.controllers.dtos.objects.created.UserCreated;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserCreatedMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    User toDomain(UserCreated userCreated);
}
