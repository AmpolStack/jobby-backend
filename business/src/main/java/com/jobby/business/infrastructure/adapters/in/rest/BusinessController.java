package com.jobby.business.infrastructure.adapters.in.rest;

import com.jobby.business.application.services.business.BusinessApplicationService;
import com.jobby.business.infrastructure.adapters.in.rest.dto.CreateBusinessDto;
import com.jobby.business.infrastructure.adapters.in.rest.dto.CreatedAddressDto;
import com.jobby.business.infrastructure.adapters.in.rest.mappers.CreateAddressMapper;
import com.jobby.business.infrastructure.adapters.in.rest.mappers.CreateBusinessMapper;
import com.jobby.infraestructure.response.definition.ApiResponseMapper;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/businesses")
public class BusinessController {
    private final CreateBusinessMapper createBusinessMapper;
    private final CreateAddressMapper createAddressMapper;
    private final ApiResponseMapper apiResponseMapper;
    private final BusinessApplicationService businessApplicationService;

    public BusinessController(CreateBusinessMapper createBusinessMapper, CreateAddressMapper createAddressMapper,
                              ApiResponseMapper apiResponseMapper,
                              BusinessApplicationService businessApplicationService) {
        this.createBusinessMapper = createBusinessMapper;
        this.createAddressMapper = createAddressMapper;
        this.apiResponseMapper = apiResponseMapper;
        this.businessApplicationService = businessApplicationService;
    }


    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CreateBusinessDto businessDto){
        var business = this.createBusinessMapper.toDomain(businessDto);
        var resp = this.businessApplicationService.createBusiness(business);
        return apiResponseMapper.map(resp);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getByValue(@PathVariable int id){
        var resp = this.businessApplicationService.getBusiness(id);
        return apiResponseMapper.map(resp);
    }

    @PatchMapping("/{id}/updateNameAndDescription")
    public ResponseEntity<?> updateNameAndDescription(@PathVariable int id, @RequestParam String name, @RequestParam String description){
        var resp = this.businessApplicationService.updateProperties(id, name, description);
        return apiResponseMapper.map(resp);
    }

    @PatchMapping("/{id}/updatePics")
    public ResponseEntity<?> updatePics(@PathVariable int id, @RequestParam String bannerImageUrl, @RequestParam String profileImageUrl){
        var resp = this.businessApplicationService.updatePics(id, bannerImageUrl, profileImageUrl);
        return apiResponseMapper.map(resp);
    }

    @PatchMapping("/{id}/updateAddress")
    public ResponseEntity<?> updatePics(@PathVariable int id, @RequestBody CreatedAddressDto createdAddressDto){
        var address = this.createAddressMapper.toDomain(createdAddressDto);
        var resp = this.businessApplicationService.updateAddress(id, address);
        return apiResponseMapper.map(resp);
    }
}
