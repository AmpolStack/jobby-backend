package com.jobby.infraestructure.autoconfiguration;

import com.jobby.domain.ports.MessagingPublisher;
import com.jobby.infraestructure.adapter.KafkaMessagingPublisher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({KafkaTemplate.class, MessagingPublisher.class})
public class MessagePublisherAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public MessagingPublisher messagingPublisher(KafkaTemplate<String,Object> kafkaTemplate) {
        return new KafkaMessagingPublisher(kafkaTemplate);
    }
}
