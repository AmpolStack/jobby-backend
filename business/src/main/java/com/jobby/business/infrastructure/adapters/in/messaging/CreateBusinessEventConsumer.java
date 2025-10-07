package com.jobby.business.infrastructure.adapters.in.messaging;

import com.jobby.business.domain.ports.out.BusinessRepository;
import com.jobby.business.infrastructure.adapters.in.messaging.mappers.SchemaBusinessMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CreateBusinessEventConsumer {
    private final SchemaBusinessMapper mapper;
    private final BusinessRepository readBusinessRepository;

    public CreateBusinessEventConsumer(SchemaBusinessMapper mapper, @Qualifier("read") BusinessRepository readBusinessRepository) {
        this.mapper = mapper;
        this.readBusinessRepository = readBusinessRepository;
    }

    @KafkaListener(topics = "com.jobby.messaging.event.business.created.v1", groupId = "com.jobby.business.consumer.created")
    public void waitingForInserts(@Payload com.jobby.messaging.schemas.Business business){
        var mapped = this.mapper.toDomain(business);
        var saved = this.readBusinessRepository.save(mapped);

        if(saved.isFailure()){
            log.error("Error in inserting business {}",mapped);
        }
    }
}
