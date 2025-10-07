package com.jobby.business.infrastructure.adapters.in.rest.mappers;

import com.jobby.business.domain.Address;
import com.jobby.business.domain.City;
import com.jobby.business.domain.Country;
import com.jobby.business.infrastructure.adapters.in.rest.dto.CreatedAddressDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressRestMapper {
    
    // Create DTO to Domain
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "city", source = "cityId", qualifiedByName = "mapCityIdToCity")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    Address fromCreateDto(CreatedAddressDto dto);
    
    // Helper method to create City from ID
    @org.mapstruct.Named("mapCityIdToCity")
    default City mapCityIdToCity(Integer cityId) {
        if (cityId == null) return null;
        City city = new City();
        city.setId(cityId);
        return city;
    }
}
