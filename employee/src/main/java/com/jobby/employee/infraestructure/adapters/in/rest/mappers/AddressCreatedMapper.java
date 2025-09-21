package com.jobby.employee.infraestructure.adapters.in.rest.controllers.dtos.mappers;

import com.jobby.employee.domain.model.Address;
import com.jobby.employee.domain.model.City;
import com.jobby.employee.infraestructure.adapters.in.rest.controllers.dtos.objects.created.AddressCreated;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface AddressCreatedMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(source = "cityId", target = "city", qualifiedByName = "cityFromId")
    Address toDomain(AddressCreated addressCreated);

    @Named("cityFromId")
    default City cityFromId(int id){
        var city = new City();
        city.setId(id);
        return city;
    }
}
