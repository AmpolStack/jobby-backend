package com.jobby.employee.infraestructure.adapters.in.messaging.consumers;

import com.jobby.messaging.schemas.EmployeeCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class CreateEmployeeEventConsumer {

    @KafkaListener(topics = "com.jobby.messaging.event.employee.created.v1", groupId = "com.jobby.employee.consumer.created")
    public void waitingForInserts(
        @Payload EmployeeCreatedEvent event,
        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
        @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
        @Header(KafkaHeaders.OFFSET) long offset
    ) {
        System.out.println("EVENT RECEIVED from topic: " + topic + 
                          ", partition: " + partition + 
                          ", offset: " + offset + 
                          ", data: " + event.toString());
    }
}
