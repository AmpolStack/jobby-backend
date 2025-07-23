package com.jobby.authorization.infraestructure.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "encrypt")
@Getter
@Setter
public class EncryptConfig {

    private Instance instance;
    private Iv iv;
    private SecretKey secretKey;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Instance{
        private String simpleName;
        private String complexName;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Iv {
        private int length;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class SecretKey {
        private String value;
        private int length;
    }

}

