package com.jobby.employee.infraestructure.persistence.jpa.mappers;

import com.jobby.employee.domain.model.User;
import com.jobby.employee.infraestructure.persistence.jpa.entities.JpaAppUserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JpaUserMapper {
    User toDomain(JpaAppUserEntity jpaAppUserEntity);
}
