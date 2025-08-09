package com.jobby.authorization.domain.shared;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TokenData {
    private int id;
    private String email;
    private String audience;
    private String issuer;
    private long msExpirationTime;
}
