package com.jobby.business.infrastructure.persistence.business.jpa.mappers;

import com.jobby.business.domain.entities.Address;
import com.jobby.business.infrastructure.persistence.business.jpa.entities.JpaAddressEntity;
import com.jobby.business.infrastructure.persistence.business.jpa.security.SecuredProperty;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {JpaCityMapper.class})
public interface JpaAddressMapper {
    @Mapping(target = "value", source = "value.rawValue")
    Address toDomain(JpaAddressEntity jpaAddressEntity);

    @Mapping(target = "value", source = "value", qualifiedByName = "toSecuredProperty")
    JpaAddressEntity toJpa(Address address);

    @Named("toSecuredProperty")
    default SecuredProperty toSecuredProperty(String value){
        var secured = new SecuredProperty();
        secured.setRawValue(value);
        return secured;
    }
}
