package com.jobby.employee.infraestructure.autoconfiguration;

import com.jobby.domain.configurations.EncryptConfig;
import com.jobby.domain.configurations.MacConfig;
import com.jobby.infraestructure.response.definition.ApiResponseMapper;
import com.jobby.infraestructure.response.implementation.problemdetails.ProblemDetailsResultMapper;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class baseAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "encrypt")
    public EncryptConfig encryptConfig() {
        return new EncryptConfig();
    }

    @Bean
    @ConfigurationProperties(prefix = "hashing.mac")
    public MacConfig macConfig() {
        return new MacConfig();
    }

    @Bean
    public ApiResponseMapper getApiResponseMapper() {
        return new ProblemDetailsResultMapper();
    }

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
