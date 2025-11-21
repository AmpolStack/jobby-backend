package com.jobby.infraestructure.security;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class SecuredPassword {
    @Transient
    private String rawValue;
    private String hashedValue;
}
