package com.jobby.authorization.infraestructure.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenRegistryResponse {
    private String token;
    private String refreshToken;
}
