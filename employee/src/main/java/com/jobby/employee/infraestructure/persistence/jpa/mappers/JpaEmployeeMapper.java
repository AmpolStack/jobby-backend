package com.jobby.employee.infraestructure.persistence.jpa.mappers;

import com.jobby.employee.domain.model.Employee;
import com.jobby.employee.infraestructure.persistence.jpa.entities.JpaEmployeeEntity;
import com.jobby.infraestructure.security.SecuredPassword;
import com.jobby.infraestructure.security.SecuredProperty;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring",
        uses = { JpaAddressMapper.class, JpaSectionalMapper.class, JpaEmployeeStatusMapper.class, JpaUserMapper.class })
public interface JpaEmployeeMapper {

    @Mapping(target = "positionName", source = "positionName.rawValue")
    @Mapping(target = "username", source = "username.rawValue")
    @Mapping(target = "password", source = "password.rawValue")
    Employee toDomain(JpaEmployeeEntity jpaEmployeeEntity);

    @Mapping(target = "positionName", source = "positionName", qualifiedByName = "toSecuredPropertyEmployee")
    @Mapping(target = "username", source = "username", qualifiedByName = "toSecuredPropertyEmployee")
    @Mapping(target = "password", source = "password", qualifiedByName = "toSecuredPasswordEmployee")
    JpaEmployeeEntity toJpa(Employee employee);

    @Named("toSecuredPropertyEmployee")
    default SecuredProperty toSecuredPropertyEmployee(String value){
        var secured = new SecuredProperty();
        secured.setRawValue(value);
        return secured;
    }

    @Named("toSecuredPasswordEmployee")
    default SecuredPassword toSecuredPasswordEmployee(String value){
        var secured = new SecuredPassword();
        secured.setRawValue(value);
        return secured;
    }
}
