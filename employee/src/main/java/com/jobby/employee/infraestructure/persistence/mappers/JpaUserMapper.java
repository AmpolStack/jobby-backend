package com.jobby.employee.infraestructure.persistence.mappers;

import com.jobby.employee.domain.model.User;
import com.jobby.employee.infraestructure.persistence.entities.JpaAppUserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JpaUserMapper {
    User toDomain(JpaAppUserEntity jpaAppUserEntity);
}
