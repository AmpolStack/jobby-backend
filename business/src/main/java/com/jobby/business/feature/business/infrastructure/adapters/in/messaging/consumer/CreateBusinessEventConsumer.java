package com.jobby.business.feature.business.infrastructure.adapters.in.messaging.consumer;

import com.jobby.business.feature.business.infrastructure.adapters.in.messaging.mappers.SchemaBusinessMapper;
import com.jobby.business.feature.business.infrastructure.adapters.out.repositories.MutationBusinessRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CreateBusinessEventConsumer {
    private final SchemaBusinessMapper mapper;
    private final MutationBusinessRepository mutationBusinessRepository;

    public CreateBusinessEventConsumer(SchemaBusinessMapper mapper,
                                       MutationBusinessRepository mutationBusinessRepository) {
        this.mapper = mapper;
        this.mutationBusinessRepository = mutationBusinessRepository;
    }

    @KafkaListener(topics = "com.jobby.messaging.event.business.created.v1", groupId = "com.jobby.business.consumer.created")
    public void waitingForInserts(@Payload com.jobby.messaging.schemas.Business business){
        var mapped = this.mapper.toDocument(business);
        var saved = this.mutationBusinessRepository.save(mapped);
        if(saved.isFailure()){
            log.error("Error in inserting business {}",mapped);
        }
    }
}
