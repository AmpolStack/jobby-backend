package com.jobby.business.infrastructure.adapters.in.rest;

import com.jobby.business.application.services.business.BusinessApplicationService;
import com.jobby.business.infrastructure.adapters.in.rest.dto.CreateBusinessDto;
import com.jobby.business.infrastructure.adapters.in.rest.dto.UpdateBusinessPicturesDto;
import com.jobby.business.infrastructure.adapters.in.rest.dto.UpdateBusinessPropertiesDto;
import com.jobby.business.infrastructure.adapters.in.rest.mappers.CreateBusinessMapper;
import com.jobby.infraestructure.response.definition.ApiResponseMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/businesses")
public class BusinessController {
    private final CreateBusinessMapper createBusinessMapper;
    private final ApiResponseMapper apiResponseMapper;
    private final BusinessApplicationService businessApplicationService;

    public BusinessController(CreateBusinessMapper createBusinessMapper,
                              ApiResponseMapper apiResponseMapper,
                              BusinessApplicationService businessApplicationService) {
        this.createBusinessMapper = createBusinessMapper;
        this.apiResponseMapper = apiResponseMapper;
        this.businessApplicationService = businessApplicationService;
    }


    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CreateBusinessDto businessDto){
        var business = this.createBusinessMapper.toDomain(businessDto);
        var resp = this.businessApplicationService.createBusiness(business);
        return apiResponseMapper.map(resp);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteById(@PathVariable int id){
        var resp = this.businessApplicationService.deleteById(id);
        return apiResponseMapper.map(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id){
        var resp = this.businessApplicationService.getBusiness(id);
        return apiResponseMapper.map(resp);
    }

    @PatchMapping("/{id}/updateProperties")
    public ResponseEntity<?> updateProperties(@PathVariable int id, @RequestBody UpdateBusinessPropertiesDto dto){
        var resp = this.businessApplicationService.updateProperties(id, dto.getName(), dto.getDescription());
        return apiResponseMapper.map(resp);
    }

    @PatchMapping("/{id}/updatePictures")
    public ResponseEntity<?> updatePics(@PathVariable int id, @RequestBody UpdateBusinessPicturesDto dto){
        var resp = this.businessApplicationService.updatePictures(id, dto.getBannerImageUrl(), dto.getProfileImageUrl());
        return apiResponseMapper.map(resp);
    }
}
