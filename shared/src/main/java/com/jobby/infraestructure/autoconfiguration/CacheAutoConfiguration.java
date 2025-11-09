package com.jobby.infraestructure.autoconfiguration;

import com.jobby.domain.ports.CacheService;
import com.jobby.infraestructure.adapter.RedisCacheService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class CacheAutoConfiguration {
    @Bean()
    @ConditionalOnMissingBean
    @ConfigurationProperties(prefix = "spring.data.redis")
    public RedisStandaloneConfiguration redisStandaloneConfiguration() {
        return new RedisStandaloneConfiguration();
    }

    @Bean()
    @ConditionalOnMissingBean
    public RedisConnectionFactory redisConnectionFactory(
            RedisStandaloneConfiguration redisStandaloneConfiguration
    ) {
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean()
    @ConditionalOnMissingBean
    public RedisTemplate<String, Object> primaryRedisTemplate(
            RedisConnectionFactory redisConnectionFactory
    ){
        RedisTemplate<String,Object> tpl = new RedisTemplate<>();
        tpl.setConnectionFactory(redisConnectionFactory);
        return tpl;
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheService cacheService(RedisTemplate<String, Object> primaryRedisTemplate) {
        return new RedisCacheService(primaryRedisTemplate);
    }
}
