package com.jobby.authorization.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TokenRegistry {
    private int id;
    private String token;
    private String refreshToken;
    private Date createdAt;
    private Date expiresAt;
}
