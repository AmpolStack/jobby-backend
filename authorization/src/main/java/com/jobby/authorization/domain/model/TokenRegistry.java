package com.jobby.authorization.domain.model;

import java.util.Date;

public class TokenRegistry {
    private int id;
    private String token;
    private String refreshToken;
    private Date createdAt;
    private Date expiresAt;
}
