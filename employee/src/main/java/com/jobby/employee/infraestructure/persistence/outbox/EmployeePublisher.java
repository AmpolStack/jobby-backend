package com.jobby.employee.infraestructure.persistence.outbox;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.error.ErrorType;
import com.jobby.domain.mobility.error.Field;
import com.jobby.domain.mobility.result.Result;
import com.jobby.employee.domain.model.Employee;
import com.jobby.employee.infraestructure.adapters.in.messaging.mappers.SchemaEmployeeMapper;
import com.jobby.employee.infraestructure.persistence.outbox.entities.JpaOutboxEventEntity;
import com.jobby.employee.infraestructure.persistence.outbox.entities.OutboxEventType;
import com.jobby.employee.infraestructure.persistence.outbox.repositories.SpringDataJpaOutboxEventEntity;
import com.jobby.infraestructure.transaction.trigger.TransactionalTrigger;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.springframework.stereotype.Component;

@Component
public class EmployeePublisher implements TransactionalTrigger<Employee, byte[]> {
    private final SchemaEmployeeMapper mapper;
    private final KafkaAvroSerializer serializer;
    private final SpringDataJpaOutboxEventEntity springDataJpaOutboxEventEntity;

    private final static String EMPLOYEE_PAYLOAD_SCHEMA_NAME = "com.jobby.messaging.event.business.created.v1";
    private final static String EMPLOYEE_PUBLISH_TOPIC = "com.jobby.messaging.event.employee.created.v1";
    private final static String AGGREGATED_TYPE = "employee";

    public EmployeePublisher(SchemaEmployeeMapper mapper,
                             KafkaAvroSerializer serializer,
                             SpringDataJpaOutboxEventEntity springDataJpaOutboxEventEntity) {
        this.mapper = mapper;
        this.serializer = serializer;
        this.springDataJpaOutboxEventEntity = springDataJpaOutboxEventEntity;
    }

    @Override
    public Result<byte[], Error> prepare(Employee input) {
        var schema = this.mapper.toSchema(input);

        try {
            byte[] avroBytes = this.serializer.serialize(EMPLOYEE_PAYLOAD_SCHEMA_NAME, schema);
            return Result.success(avroBytes);

        } catch (Exception e) {
            return Result.failure(ErrorType.ITS_SERIALIZATION_ERROR,  new Field("Business", "Error in outbox publication"));
        }
    }

    @Override
    public void send(byte[] input1, Employee input2) {
        JpaOutboxEventEntity event = JpaOutboxEventEntity.builder()
                .aggregateType(AGGREGATED_TYPE)
                .aggregateId(String.valueOf(input2.getId()))
                .eventType(OutboxEventType.CREATED)
                .topic(EMPLOYEE_PUBLISH_TOPIC)
                .payload(input1)
                .build();

        this.springDataJpaOutboxEventEntity.save(event);
    }
}
