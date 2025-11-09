package com.jobby.business.feature.address.infrastructure.persistence.jpa;

import com.jobby.business.feature.address.domain.Address;
import com.jobby.business.feature.city.infrastructure.persistence.jpa.JpaCityMapper;
import com.jobby.infraestructure.security.SecuredProperty;
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
