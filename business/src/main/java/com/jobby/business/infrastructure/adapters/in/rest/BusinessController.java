package com.jobby.business.infrastructure.adapters.in.rest;

import com.jobby.business.application.services.business.BusinessCommandService;
import com.jobby.business.domain.ports.in.GetBusinessByAddressValueUseCase;
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
    private final GetBusinessByAddressValueUseCase getBusinessByAddressValueUseCase;

    private final BusinessCommandService businessCommandService;

    public BusinessController(CreateBusinessMapper createBusinessMapper, ApiResponseMapper apiResponseMapper, GetBusinessByAddressValueUseCase getBusinessByAddressValueUseCase, BusinessCommandService businessCommandService) {
        this.createBusinessMapper = createBusinessMapper;
        this.apiResponseMapper = apiResponseMapper;
        this.getBusinessByAddressValueUseCase = getBusinessByAddressValueUseCase;
        this.businessCommandService = businessCommandService;
    }


    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CreateBusinessDto businessDto){
        var business = this.createBusinessMapper.toDomain(businessDto);
        var resp = this.businessCommandService.createBusiness(business);
        return apiResponseMapper.map(resp);
    }


    @GetMapping("/getByValue")
    public ResponseEntity<?> getByValue(@RequestParam String value){
        var resp = this.getBusinessByAddressValueUseCase.execute(value);
        return apiResponseMapper.map(resp);

    }
}
