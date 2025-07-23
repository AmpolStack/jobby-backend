package com.jobby.authorization.infraestructure.custom;

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
    public static class Instance{
        private String simpleName;
        private String complexName;
    }

    @Getter
    @Setter
    public static class Iv {
        private int length;
    }

    @Getter
    @Setter
    public static class SecretKey {
        private String value;
        private int length;
    }

}

