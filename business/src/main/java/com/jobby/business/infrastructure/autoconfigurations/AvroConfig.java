package com.jobby.business.infrastructure.autoconfigurations;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Map;

@Configuration
public class AvroConfig {

    @Bean
    public KafkaAvroSerializer kafkaAvroSerializer(
            @Value("${spring.kafka.producer.properties.schema.registry.url}") String registryUrl,
            @Value("${spring.kafka.producer.properties.auto.register.schemas}") boolean autoRegister
    ) {
        KafkaAvroSerializer serializer = new KafkaAvroSerializer();
        serializer.configure(
                Map.of(
                        "schema.registry.url", registryUrl,
                        "auto.register.schemas", autoRegister
                ),
                false
        );
        return serializer;
    }
}
