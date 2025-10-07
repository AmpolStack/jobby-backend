package com.jobby.business.infrastructure.adapters.in.rest;

import com.jobby.business.domain.ports.in.CreateBusinessUseCase;
import com.jobby.business.domain.ports.in.GetBusinessByAddressValueUseCase;
import com.jobby.business.infrastructure.adapters.in.messaging.mappers.SchemaBusinessMapper;
import com.jobby.business.infrastructure.adapters.in.rest.dto.CreateBusinessDto;
import com.jobby.business.infrastructure.adapters.in.rest.mappers.CreateBusinessMapper;
import com.jobby.domain.ports.MessagingPublisher;
import com.jobby.infraestructure.response.definition.ApiResponseMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/businesses")
public class BusinessController {
    private final CreateBusinessMapper createBusinessMapper;
    private final ApiResponseMapper apiResponseMapper;
    private final MessagingPublisher messagingPublisher;
    private final SchemaBusinessMapper schemaBusinessMapper;
    private final GetBusinessByAddressValueUseCase getBusinessByAddressValueUseCase;
    private final CreateBusinessUseCase createBusinessUseCase;

    public BusinessController(CreateBusinessMapper createBusinessMapper, ApiResponseMapper apiResponseMapper, MessagingPublisher messagingPublisher, SchemaBusinessMapper schemaBusinessMapper, GetBusinessByAddressValueUseCase getBusinessByAddressValueUseCase, CreateBusinessUseCase createBusinessUseCase) {
        this.createBusinessMapper = createBusinessMapper;
        this.schemaBusinessMapper = schemaBusinessMapper;
        this.apiResponseMapper = apiResponseMapper;
        this.messagingPublisher = messagingPublisher;
        this.getBusinessByAddressValueUseCase = getBusinessByAddressValueUseCase;
        this.createBusinessUseCase = createBusinessUseCase;
    }


    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CreateBusinessDto businessDto){
        var business = this.createBusinessMapper.toDomain(businessDto);
        var resp = this.createBusinessUseCase.execute(business);

        resp.fold(
                (onSuccess) -> {
                        var schemaBusiness = this.schemaBusinessMapper.toSchema(onSuccess);
                        this.messagingPublisher.publishAsync("com.jobby.messaging.event.business.created.v1", schemaBusiness);
                    },
                (onFailure) -> {

                    }
                );

        return apiResponseMapper.map(resp);
    }


    @GetMapping("/getByValue")
    public ResponseEntity<?> getByValue(@RequestParam String value){
        var resp = this.getBusinessByAddressValueUseCase.execute(value);
        return apiResponseMapper.map(resp);

    }
}
