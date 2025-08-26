package com.jobby.gateway.domain;

import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;
import java.util.Map;

public interface ReactiveJwtHeaderTransformer {
    Mono<Map<String, String>> toHeaders(Authentication authentication);
}
