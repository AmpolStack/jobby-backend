package com.jobby.business.feature.business.infrastructure.adapters.in.messaging.consumer;

import com.jobby.business.feature.business.application.services.BusinessEventExecutor;
import com.jobby.business.feature.business.application.useCase.events.BusinessDefaultSaveEvent;
import com.jobby.business.feature.business.infrastructure.adapters.in.messaging.mappers.SchemaBusinessMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CreateBusinessEventConsumer {
    private final SchemaBusinessMapper mapper;
    private final BusinessEventExecutor businessEventExecutor;

    public CreateBusinessEventConsumer(SchemaBusinessMapper mapper, BusinessEventExecutor businessEventExecutor) {
        this.mapper = mapper;
        this.businessEventExecutor = businessEventExecutor;
    }

    @KafkaListener(topics = "com.jobby.messaging.event.business.created.v1", groupId = "com.jobby.business.consumer.created")
    public void waitingForInserts(@Payload com.jobby.messaging.schemas.Business business){
        var mapped = this.mapper.toDomain(business);
        var event = new BusinessDefaultSaveEvent(mapped);
        var saved = this.businessEventExecutor.execute(event);
        if(saved.isFailure()){
            log.error("Error in inserting business {}",mapped);
        }
    }
}
