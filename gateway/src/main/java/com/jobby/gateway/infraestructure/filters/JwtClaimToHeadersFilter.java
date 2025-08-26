package com.jobby.gateway.infraestructure.filters;

import com.jobby.gateway.domain.ReactiveJsonWebTokenService;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
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
        String path = exchange.getRequest().getPath().value();
        if (path.startsWith("/authorize")) {
            return chain.filter(exchange);
        }

        var cleanedReq = exchange.getRequest().mutate()
                .headers(h -> FORBIDDEN.forEach(h::remove))
                .build();

        var cleanedExchange = exchange.mutate()
                .request(cleanedReq).build();

        var authHeader = cleanedExchange.getRequest()
                .getHeaders().getFirst("Authorization");

        if(authHeader != null && authHeader.isBlank()){
            return chain.filter(cleanedExchange);
        }

        return this.decoder.decode(authHeader)
                .flatMap(jwt -> this.decoder.toAuthentication(jwt)
                        .flatMap(authentication -> this.decoder.toHeaders(jwt)
                                .flatMap(headersMap -> {
                                   var mutated = cleanedExchange.getRequest().mutate()
                                           .headers(h -> headersMap.forEach((k, v) ->{
                                               if(v != null && !v.isBlank()) h.add(k,v);
                                           }))
                                           .build();

                                   var withHeaders = cleanedExchange.mutate()
                                           .request(mutated).build();

                                   return chain.filter(withHeaders)
                                           .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
                                })
                        )
                ).onErrorResume(ex -> {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                });
    }
}
