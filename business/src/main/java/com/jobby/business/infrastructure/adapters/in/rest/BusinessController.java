package com.jobby.business.infrastructure.adapters.in.rest;

import com.jobby.business.application.services.BusinessCommandExecutor;
import com.jobby.business.application.services.BusinessQueryExecutor;
import com.jobby.business.application.useCase.*;
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
    private final BusinessCommandExecutor businessCommandExecutor;
    private final BusinessQueryExecutor businessQueryExecutor;


    public BusinessController(CreateBusinessMapper createBusinessMapper,
                              ApiResponseMapper apiResponseMapper, BusinessCommandExecutor businessCommandExecutor, BusinessQueryExecutor businessQueryExecutor) {
        this.createBusinessMapper = createBusinessMapper;
        this.apiResponseMapper = apiResponseMapper;
        this.businessCommandExecutor = businessCommandExecutor;
        this.businessQueryExecutor = businessQueryExecutor;
    }


    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CreateBusinessDto businessDto){
        var business = this.createBusinessMapper.toDomain(businessDto);
        var command = new BusinessDefaultCreateCommand(business);
        var resp = this.businessCommandExecutor.execute(command);
        return apiResponseMapper.map(resp);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteById(@PathVariable int id){
        var command = new BusinessDeleteByIdCommand(id);
        var resp = this.businessCommandExecutor.execute(command);
        return apiResponseMapper.map(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id){
        var query = new BusinessQueryById(id);
        var resp = this.businessQueryExecutor.execute(query);
        return apiResponseMapper.map(resp);
    }

    @PatchMapping("/{id}/updateProperties")
    public ResponseEntity<?> updateProperties(@PathVariable int id, @RequestBody UpdateBusinessPropertiesDto dto){
        var command = new UpdateBusinessPropertiesCommand(id, dto.getName(), dto.getDescription());
        var resp = this.businessCommandExecutor.execute(command);
        return apiResponseMapper.map(resp);
    }

    @PatchMapping("/{id}/updatePictures")
    public ResponseEntity<?> updatePics(@PathVariable int id, @RequestBody UpdateBusinessPicturesDto dto){
        var command = new UpdateBusinessPicturesCommand(id, dto.getBannerImageUrl(), dto.getProfileImageUrl());
        var resp = this.businessCommandExecutor.execute(command);
        return apiResponseMapper.map(resp);
    }
}
