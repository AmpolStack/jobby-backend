package com.jobby.business.feature.business.infrastructure.internal;

import com.jobby.business.feature.business.domain.entities.Business;
import com.jobby.business.feature.business.infrastructure.adapters.in.messaging.mappers.SchemaBusinessMapper;
import com.jobby.business.feature.outbox.infrastructure.entities.OutboxEventType;
import com.jobby.infraestructure.transaction.trigger.TransactionalTrigger;
import com.jobby.business.feature.outbox.infrastructure.entities.JpaOutboxEventEntity;
import com.jobby.business.feature.outbox.infrastructure.repositories.SpringDataJpaOutboxEventEntity;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.error.ErrorType;
import com.jobby.domain.mobility.error.Field;
import com.jobby.domain.mobility.result.Result;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.springframework.stereotype.Component;

@Component
public class BusinessPublisher implements TransactionalTrigger<Business, byte[]> {
    private final SchemaBusinessMapper schemaBusinessMapper;
    private final KafkaAvroSerializer kafkaAvroSerializer;
    private final SpringDataJpaOutboxEventEntity springDataOutboxEventEntity;

    private final static String BUSINESS_PAYLOAD_SCHEMA_NAME = "com.jobby.messaging.event.business.created.v1";
    private final static String BUSINESS_PUBLISH_TOPIC = "com.jobby.messaging.event.business.created.v1";
    private final static String AGGREGATED_TYPE = "business";

    public BusinessPublisher(SchemaBusinessMapper schemaBusinessMapper,
                             KafkaAvroSerializer kafkaAvroSerializer,
                             SpringDataJpaOutboxEventEntity springDataOutboxEventEntity) {
        this.schemaBusinessMapper = schemaBusinessMapper;
        this.kafkaAvroSerializer = kafkaAvroSerializer;
        this.springDataOutboxEventEntity = springDataOutboxEventEntity;
    }

    @Override
    public Result<byte[], Error> prepare(Business input) {
        var businessSchema = this.schemaBusinessMapper.toSchema(input);

        try {
            byte[] avroBytes = this.kafkaAvroSerializer.serialize(BUSINESS_PAYLOAD_SCHEMA_NAME, businessSchema);
            return Result.success(avroBytes);

        } catch (Exception e) {
            return Result.failure(ErrorType.ITS_SERIALIZATION_ERROR,  new Field("Business", "Error in outbox publication"));
        }
    }

    @Override
    public void send(byte[] payload, Business business){
        JpaOutboxEventEntity event = JpaOutboxEventEntity.builder()
                .aggregateType(AGGREGATED_TYPE)
                .aggregateId(String.valueOf(business.getId()))
                .eventType(OutboxEventType.CREATED)
                .topic(BUSINESS_PUBLISH_TOPIC)
                .payload(payload)
                .build();

        this.springDataOutboxEventEntity.save(event);
    }

}
