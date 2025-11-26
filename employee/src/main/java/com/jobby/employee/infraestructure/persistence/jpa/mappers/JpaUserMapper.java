package com.jobby.employee.infraestructure.persistence.jpa.mappers;

import com.jobby.employee.domain.model.User;
import com.jobby.employee.infraestructure.persistence.jpa.entities.JpaAppUserEntity;
import com.jobby.infraestructure.security.SecuredProperty;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface JpaUserMapper {

    @Mapping(target = "lastName", source = "lastName.rawValue")
    @Mapping(target = "email", source = "email.rawValue")
    @Mapping(target = "phone", source = "phone.rawValue")
    @Mapping(target = "firstName", source = "firstName.rawValue")
    User toDomain(JpaAppUserEntity jpaAppUserEntity);

    @Mapping(target = "lastName", source = "lastName", qualifiedByName = "toSecuredProperty")
    @Mapping(target = "email", source = "email", qualifiedByName = "toSecuredProperty")
    @Mapping(target = "phone", source = "phone", qualifiedByName = "toSecuredProperty")
    @Mapping(target = "firstName", source = "firstName", qualifiedByName = "toSecuredProperty")
    JpaAppUserEntity toJpa(User user);

    @Named("toSecuredProperty")
    default SecuredProperty toSecuredProperty(String value){
        var secured = new SecuredProperty();
        secured.setRawValue(value);
        return secured;
    }
}
