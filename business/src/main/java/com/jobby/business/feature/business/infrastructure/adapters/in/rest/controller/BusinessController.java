package com.jobby.business.feature.business.infrastructure.adapters.in.rest.controller;

import com.jobby.business.feature.business.application.services.BusinessCommandExecutor;
import com.jobby.business.feature.business.application.services.BusinessQueryExecutor;
import com.jobby.business.feature.business.application.useCase.commands.BusinessDefaultCreateCommand;
import com.jobby.business.feature.business.application.useCase.commands.DeleteByIdBusinessCommand;
import com.jobby.business.feature.business.application.useCase.commands.UpdatePicturesBusinessCommand;
import com.jobby.business.feature.business.application.useCase.commands.UpdatePropertiesBusinessCommand;
import com.jobby.business.feature.business.application.useCase.queries.BusinessQueryById;
import com.jobby.business.feature.business.application.useCase.queries.BusinessSetQueryByCityId;
import com.jobby.business.feature.business.application.useCase.queries.BusinessSetQueryByCountryId;
import com.jobby.business.feature.business.infrastructure.adapters.in.rest.dto.CreateBusinessDto;
import com.jobby.business.feature.business.infrastructure.adapters.in.rest.dto.UpdateBusinessPicturesDto;
import com.jobby.business.feature.business.infrastructure.adapters.in.rest.dto.UpdateBusinessPropertiesDto;
import com.jobby.business.feature.business.infrastructure.adapters.in.rest.mappers.CreateBusinessMapper;
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
        var command = new DeleteByIdBusinessCommand(id);
        var resp = this.businessCommandExecutor.execute(command);
        return apiResponseMapper.map(resp);
    }

    @GetMapping("byCity/{cityId}")
    public ResponseEntity<?> getByCityId(@PathVariable int cityId){
        var query = new BusinessSetQueryByCityId(cityId);
        var resp = this.businessQueryExecutor.execute(query);
        return apiResponseMapper.map(resp);
    }

    @GetMapping("byCountry/{countryId}")
    public ResponseEntity<?> getByCountryId(@PathVariable int countryId){
        var query = new BusinessSetQueryByCountryId(countryId);
        var resp = this.businessQueryExecutor.execute(query);
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
        var command = new UpdatePropertiesBusinessCommand(id, dto.getName(), dto.getDescription());
        var resp = this.businessCommandExecutor.execute(command);
        return apiResponseMapper.map(resp);
    }

    @PatchMapping("/{id}/updatePictures")
    public ResponseEntity<?> updatePics(@PathVariable int id, @RequestBody UpdateBusinessPicturesDto dto){
        var command = new UpdatePicturesBusinessCommand(id, dto.getBannerImageUrl(), dto.getProfileImageUrl());
        var resp = this.businessCommandExecutor.execute(command);
        return apiResponseMapper.map(resp);
    }
}
