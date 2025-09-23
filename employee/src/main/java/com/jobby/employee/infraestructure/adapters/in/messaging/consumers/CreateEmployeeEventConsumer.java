package com.jobby.employee.infraestructure.adapters.in.messaging.consumers;

import com.jobby.employee.domain.ports.in.CreateEmployeeFromEventUseCase;
import com.jobby.employee.infraestructure.adapters.in.messaging.mappers.SchemaEmployeeMapper;
import com.jobby.messaging.schemas.EmployeeCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CreateEmployeeEventConsumer {

    private final CreateEmployeeFromEventUseCase createEmployeeFromEventUseCase;
    private final SchemaEmployeeMapper mapper;

    public CreateEmployeeEventConsumer(CreateEmployeeFromEventUseCase createEmployeeFromEventUseCase,
                                       SchemaEmployeeMapper mapper) {
        this.createEmployeeFromEventUseCase = createEmployeeFromEventUseCase;
        this.mapper = mapper;
    }

    @KafkaListener(topics = "com.jobby.messaging.event.employee.created.v1", groupId = "com.jobby.employee.consumer.created")
    public void waitingForInserts(@Payload EmployeeCreatedEvent event)
    {
        var domainEmployee = this.mapper.toDomain(event);
        this.createEmployeeFromEventUseCase.execute(domainEmployee).fold(
                onSuccess -> log.info("Created employee from event {}", onSuccess.toString()),
                onFailure -> log.error("Failed to create employee from event {}", onFailure.toString()));
    }
}
