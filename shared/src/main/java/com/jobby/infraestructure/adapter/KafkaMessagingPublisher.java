package com.jobby.infraestructure.adapter;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.error.ErrorType;
import com.jobby.domain.mobility.error.Field;
import com.jobby.domain.mobility.result.Result;
import com.jobby.domain.ports.MessagingPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class KafkaMessagingPublisher implements MessagingPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaMessagingPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public <T> Result<Void, Error> publish(String topicIdentifier, T data, int timeout) {
        try {
            var future = kafkaTemplate.send(topicIdentifier, data);

            future.get(10, TimeUnit.SECONDS);

            return Result.success(null);

        } catch (TimeoutException e) {
            return Result.failure(ErrorType.ITS_EXTERNAL_SERVICE_FAILURE, new Field("Kafka", "Timeout exceeded in message sending"));
        } catch (ExecutionException e) {
            return Result.failure(ErrorType.ITS_EXTERNAL_SERVICE_FAILURE, new Field("Kafka", "Execution error in message sending"));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Result.failure(ErrorType.ITS_OPERATION_ERROR, new Field("Kafka", "Interrupted Operation"));
        }
    }

    @Override
    public <T> void publishAsync(String topicIdentifier, T data) {
        var future = kafkaTemplate.send(topicIdentifier, data);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println(
                        "[KAFKA-PUBLISH] Successfully sent message to topic '"
                                + result.getRecordMetadata().topic()
                                + "' | partition=" + result.getRecordMetadata().partition()
                                + " | offset=" + result.getRecordMetadata().offset()
                                + " | data=" + data
                );
            } else {
                System.out.println(
                        "[KAFKA-PUBLISH] Failed to send message to topic '"
                                + topicIdentifier
                                + "' | data=" + data
                                + " | reason=" + ex.getMessage()
                );
            }
        });
    }
}
