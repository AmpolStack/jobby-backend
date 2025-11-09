package com.jobby.business.infrastructure.persistence.business.jpa.security;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class SecuredProperty {
    @Transient
    private String rawValue;
    private String encryptedValue;
    private byte[] hashedValue;
}
