package com.jobby.gateway.domain;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Mono;
import java.util.Map;

public interface ReactiveJsonWebTokenService {
    Mono<Jwt> decode(String token);
    Mono<Authentication> toAuthentication(Jwt jwt);
    Mono<Map<String, String>> toHeaders(Jwt jwt);
    Mono<Map<String, String>> toHeaders(Authentication authentication);
}
