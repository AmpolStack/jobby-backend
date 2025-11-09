package com.jobby.business.feature.address.infrastructure.persistence.mongo;

import com.jobby.business.feature.address.domain.Address;
import com.jobby.business.feature.city.infrastructure.persistence.mongo.MongoCityMapper;
import com.jobby.infraestructure.security.SecuredProperty;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {MongoCityMapper.class})
public interface MongoAddressMapper {
    @Mapping(target = "value", source = "value.rawValue")
    Address toDomain(MongoAddressEntity mongoAddressEntity);

    @Mapping(target = "value", source = "value", qualifiedByName = "toSecuredProperty")
    MongoAddressEntity toDocument(Address address);

    @Named("toSecuredProperty")
    default SecuredProperty toSecuredProperty(String value){
        var secured = new SecuredProperty();
        secured.setRawValue(value);
        return secured;
    }
}
