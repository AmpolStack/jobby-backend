package com.jobby.gateway.infraestructure.filters;

import com.jobby.gateway.domain.ReactiveJsonWebTokenService;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Order()
public class JwtClaimToHeadersFilter implements GlobalFilter {
    private static final List<String> FORBIDDEN = List.of("X-User-Id", "X-Roles", "X-Username");
    private final ReactiveJsonWebTokenService decoder;

    public JwtClaimToHeadersFilter(ReactiveJsonWebTokenService decoder) {
        this.decoder = decoder;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var cleanedReq = exchange.getRequest().mutate()
                .headers(h -> FORBIDDEN.forEach(h::remove))
                .build();

        var cleanedExchange = exchange.mutate()
                .request(cleanedReq).build();

        //noinspection ReactorTransformationOnMonoVoid
        return ReactiveSecurityContextHolder.getContext()
                .flatMap(ctx -> {
                    var authentication = ctx.getAuthentication();
                    if (authentication == null || !authentication.isAuthenticated()) {
                        return chain.filter(cleanedExchange);
                    }

                    var authHeader = cleanedExchange.getRequest()
                            .getHeaders().getFirst("Authorization");

                    return this.decoder.decode(authHeader)
                            .flatMap(auth -> decoder.toHeaders(auth)
                                    .flatMap(headersMap -> {
                                        var mutated = cleanedExchange.getRequest().mutate()
                                                .headers(h -> headersMap.forEach((k, v) -> {
                                                    if (v != null && !v.isBlank()) h.add(k, v);
                                                }))
                                                .build();

                                        var withHeaders = cleanedExchange.mutate()
                                                .request(mutated).build();

                                        return chain.filter(withHeaders);
                                    })
                            );
                })
                .switchIfEmpty(chain.filter(cleanedExchange));
    }
}
