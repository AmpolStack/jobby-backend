package com.jobby.business.infrastructure.adapters.out.messaging;

import com.jobby.business.domain.entities.Business;
import com.jobby.business.domain.ports.out.messaging.BusinessMessagePublisher;
import com.jobby.business.infrastructure.adapters.in.messaging.mappers.SchemaBusinessMapper;
import com.jobby.business.infrastructure.persistence.outbox.entities.OutboxEventEntity;
import com.jobby.business.infrastructure.persistence.outbox.repositories.SpringDataOutboxEventEntity;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.error.ErrorType;
import com.jobby.domain.mobility.error.Field;
import com.jobby.domain.mobility.result.Result;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component("byOutbox")
@Primary
public class OutBoxBusinessEventMessagePublisher implements BusinessMessagePublisher {
    private final KafkaAvroSerializer kafkaAvroSerializer;
    private final SchemaBusinessMapper schemaBusinessMapper;
    private final SpringDataOutboxEventEntity springDataOutboxEventEntity;

    public OutBoxBusinessEventMessagePublisher(KafkaAvroSerializer kafkaAvroSerializer, SchemaBusinessMapper schemaBusinessMapper, SpringDataOutboxEventEntity springDataOutboxEventEntity) {
        this.kafkaAvroSerializer = kafkaAvroSerializer;
        this.schemaBusinessMapper = schemaBusinessMapper;
        this.springDataOutboxEventEntity = springDataOutboxEventEntity;
    }

    @Override
    public Result<Void, Error> sendBusiness(Business business) {
        try {

            var businessSchema = this.schemaBusinessMapper.toSchema(business);
            byte[] avroBytes = this.kafkaAvroSerializer.serialize("com.jobby.messaging.event.business.created.v1", businessSchema);

            OutboxEventEntity event = OutboxEventEntity.builder()
                    .aggregateType("business")
                    .aggregateId(business.getId().toString())
                    .eventType(OutboxEventEntity.EventType.CREATED)
                    .topic("com.jobby.messaging.event.business.created.v1")
                    .payload(avroBytes)
                    .build();

            var resp = this.springDataOutboxEventEntity.save(event);
        } catch (Exception e) {
            return Result.failure(ErrorType.ITS_UNKNOWN_ERROR, new Field("Business", "Error in outbox publication"));
        }

        return Result.success(null);
    }
}
