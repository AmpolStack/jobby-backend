package com.jobby.authorization.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenRegistry {
    private int id;
    private String token;
    private String refreshToken;
    private Date createdAt;
    private Date expiresAt;
}
