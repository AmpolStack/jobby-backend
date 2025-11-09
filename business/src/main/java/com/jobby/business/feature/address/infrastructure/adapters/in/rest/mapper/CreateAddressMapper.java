package com.jobby.business.feature.address.infrastructure.adapters.in.rest.mapper;

import com.jobby.business.feature.address.domain.Address;
import com.jobby.business.feature.address.infrastructure.adapters.in.rest.dto.CreatedAddressDto;
import com.jobby.business.feature.city.domain.City;
import com.jobby.business.feature.country.domain.Country;
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
