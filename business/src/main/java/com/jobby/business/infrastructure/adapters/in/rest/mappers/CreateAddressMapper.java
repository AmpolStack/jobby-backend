package com.jobby.business.infrastructure.adapters.in.rest.mappers;

import com.jobby.business.domain.entities.Address;
import com.jobby.business.domain.entities.City;
import com.jobby.business.domain.entities.Country;
import com.jobby.business.infrastructure.adapters.in.rest.dto.CreatedAddressDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CreateAddressMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "city", expression = "java(fromIdToCity(createdAddressDto.getCityId(), createdAddressDto.getCountryId()))")
    Address toDomain(CreatedAddressDto createdAddressDto);

    @Named("fromIdToCity")
    default City fromIdToCity(Integer cityId, Integer countryId) {
        City city = new City();
        city.setId(cityId);
        Country country = new Country();
        country.setId(countryId);
        city.setCountry(country);
        return city;
    }
}
