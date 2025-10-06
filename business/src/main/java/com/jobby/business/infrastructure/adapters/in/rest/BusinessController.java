package com.jobby.business.infrastructure.adapters.in.rest;

import com.jobby.business.domain.ports.BusinessRepository;
import com.jobby.business.infrastructure.adapters.in.messaging.mappers.SchemaBusinessMapper;
import com.jobby.business.infrastructure.adapters.in.rest.dto.CreateBusinessDto;
import com.jobby.business.infrastructure.adapters.in.rest.mappers.CreateBusinessMapper;
import com.jobby.domain.ports.MessagingPublisher;
import com.jobby.infraestructure.response.definition.ApiResponseMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/business")
public class BusinessController {

    private final BusinessRepository businessRepository;
    private final CreateBusinessMapper createBusinessMapper;
    private final ApiResponseMapper apiResponseMapper;
    private final MessagingPublisher messagingPublisher;
    private final SchemaBusinessMapper schemaBusinessMapper;

    public BusinessController(@Qualifier("write") BusinessRepository businessRepository, CreateBusinessMapper createBusinessMapper, ApiResponseMapper apiResponseMapper, MessagingPublisher messagingPublisher, SchemaBusinessMapper schemaBusinessMapper) {
        this.businessRepository = businessRepository;
        this.createBusinessMapper = createBusinessMapper;
        this.apiResponseMapper = apiResponseMapper;
        this.messagingPublisher = messagingPublisher;
        this.schemaBusinessMapper = schemaBusinessMapper;
    }


    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CreateBusinessDto businessDto){
        var business = this.createBusinessMapper.toDomain(businessDto);
        var resp = this.businessRepository.save(business)
                .flatMap(businessSaved -> this.businessRepository.findById(businessSaved.getId()));

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

}
