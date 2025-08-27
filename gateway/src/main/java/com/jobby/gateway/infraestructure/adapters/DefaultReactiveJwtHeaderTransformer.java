package com.jobby.gateway.infraestructure.adapters;

import com.jobby.gateway.domain.ReactiveJwtHeaderTransformer;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.HashMap;
import java.util.Map;

@Service
public class DefaultReactiveJwtHeaderTransformer implements ReactiveJwtHeaderTransformer {

    @Override
    public Mono<Map<String, String>> toHeaders(Authentication authentication) {
        Map<String, String> headers = new HashMap<>();

        if (!(authentication instanceof JwtAuthenticationToken jwtAuth)) {
            return Mono.just(headers);
        }

        jwtAuth.getTokenAttributes().forEach((k, v) -> {
            System.out.println(k + " : " + v);
        });


        var email = jwtAuth.getTokenAttributes().get("com.jobby.employee.email");
        if(email != null && !(email instanceof String)) headers.put("X-User-Email", email.toString());

        Jwt jwt = jwtAuth.getToken();

        var subject = jwt.getSubject();
        if (subject != null){
            headers.put("X-User-Id", subject);
        }

        return Mono.just(headers);
    }
}
