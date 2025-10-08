package com.jobby.business.infrastructure.adapters.in.rest;

import com.jobby.business.application.services.business.BusinessApplicationService;
import com.jobby.business.infrastructure.adapters.in.rest.dto.CreateBusinessDto;
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


    @GetMapping("/{id}")
    public ResponseEntity<?> getByValue(@PathVariable int id){
        var resp = this.businessApplicationService.getBusiness(id);
        return apiResponseMapper.map(resp);
    }
}
