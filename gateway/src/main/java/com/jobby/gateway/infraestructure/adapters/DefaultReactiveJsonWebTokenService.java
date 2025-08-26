package com.jobby.gateway.infraestructure.adapters;

import com.jobby.gateway.domain.ReactiveJsonWebTokenService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DefaultReactiveJsonWebTokenService implements ReactiveJsonWebTokenService {
    private final ReactiveJwtDecoder jwtDecoder;
    private final JwtGrantedAuthoritiesConverter authoritiesConverter;

    public DefaultReactiveJsonWebTokenService(ReactiveJwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
        this.authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        this.authoritiesConverter.setAuthoritiesClaimName("roles");
        this.authoritiesConverter.setAuthorityPrefix("ROLE_");
    }


    @Override
    public Mono<Jwt> decode(String token) {
        if (token == null) return Mono.error(new IllegalArgumentException("Authorization header vacío"));
        if (token.startsWith("Bearer ")) token = token.substring(7);
        return jwtDecoder.decode(token);
    }

    @Override
    public Mono<Authentication> toAuthentication(Jwt jwt) {
        Collection<GrantedAuthority> auths = authoritiesConverter.convert(jwt);
        return Mono.just(new JwtAuthenticationToken(jwt, auths));
    }

    @Override
    public Mono<Map<String, String>> toHeaders(Jwt jwt) {
        Map<String, String> headers = new HashMap<>();
        if (jwt.getSubject() != null) headers.put("X-User-Id", jwt.getSubject());

        String username = jwt.getClaimAsString("preferred_username");
        if (username == null) username = jwt.getClaimAsString("username");
        if (username != null) headers.put("X-Username", username);

        if (jwt.hasClaim("roles")) {
            List<String> roles = jwt.getClaimAsStringList("roles");
            if (roles != null && !roles.isEmpty()) headers.put("X-Roles", String.join(",", roles));
        }

        return Mono.just(headers);
    }
}
