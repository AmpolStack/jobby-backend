package com.jobby.gateway.infraestructure.filters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.util.*;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;


@Component
public class DynamicHeaderGatewayFilterFactory
        extends AbstractGatewayFilterFactory<DynamicHeaderGatewayFilterFactory.Config> {

    private static final Logger logger = LoggerFactory.getLogger(DynamicHeaderGatewayFilterFactory.class);
    private final SpelExpressionParser spelParser = new SpelExpressionParser();

    public DynamicHeaderGatewayFilterFactory() {
        super(Config.class);
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HeaderConfig {
        private String name;
        private String value;
    }

    @Setter
    @Getter
    public static class Config {
        private List<HeaderConfig> headers = new ArrayList<>();
    }

    @Override
    public GatewayFilter apply(Config config) {
        List<HeaderConfig> headers = config.getHeaders();
        if (headers == null || headers.isEmpty()) {
            return (exchange, chain) -> chain.filter(exchange);
        }

        return (exchange, chain) -> {
            // get Authentication reactively from SecurityContext
            Mono<Authentication> authMono = org.springframework.security.core.context.ReactiveSecurityContextHolder.getContext()
                    .flatMap(ctx -> Mono.justOrEmpty(ctx.getAuthentication()))
                    .onErrorResume(e -> {
                        logger.debug("Error reading SecurityContext: {}", e.toString());
                        return Mono.empty();
                    });

            // If there is a principal, we evaluate it; if not, we evaluate it anyway (with a null principal and emptyMap claims)
            //noinspection ReactorTransformationOnMonoVoid
            return authMono
                    .flatMap(auth -> evaluateAndApply(exchange, chain, headers, auth))
                    .switchIfEmpty(evaluateAndApply(exchange, chain, headers, null));
        };
    }

    private Mono<Void> evaluateAndApply(ServerWebExchange exchange,
                                        org.springframework.cloud.gateway.filter.GatewayFilterChain chain,
                                        List<HeaderConfig> headers,
                                        Authentication principal) {
        Map<String, Object> claims = Collections.emptyMap();
        if (principal instanceof JwtAuthenticationToken) {
            try {
                claims = ((JwtAuthenticationToken) principal).getToken().getClaims();
            } catch (Exception ex) {
                logger.debug("Could not extract claims from JwtAuthenticationToken: {}", ex.toString());
                claims = Collections.emptyMap();
            }
        }

        // prepare SpEL context (available variables: #exchange, #request, #principal, #claims)
        StandardEvaluationContext spelCtx = new StandardEvaluationContext();
        spelCtx.setVariable("exchange", exchange);
        spelCtx.setVariable("request", exchange.getRequest());
        spelCtx.setVariable("principal", principal);
        spelCtx.setVariable("claims", claims);

        // build mutated request: first we remove headers that we are going to inject to avoid spoofing
        ServerHttpRequest.Builder requestBuilder = exchange.getRequest().mutate();
        requestBuilder.headers(httpHeaders -> {
            for (HeaderConfig hc : headers) {
                if (hc != null && hc.getName() != null) {
                    httpHeaders.remove(hc.getName());
                }
            }
        });

        // evaluate and add headers (only if the resolved value is not null/blank)
        for (HeaderConfig hc : headers) {
            if (hc == null || hc.getName() == null) continue;
            String resolved = resolveValue(hc.getValue(), spelCtx);
            if (resolved != null && !resolved.isBlank()) {
                requestBuilder.header(hc.getName(), resolved);
            } else {
                logger.debug("Header '{}' not added because resolved value is null/blank (raw='{}')",
                        hc.getName(), hc.getValue());
            }
        }

        ServerHttpRequest mutatedRequest = requestBuilder.build();
        ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();
        return chain.filter(mutatedExchange);
    }

    private String resolveValue(String raw, StandardEvaluationContext ctx) {
        if (raw == null) return null;
        raw = raw.trim();
        if (raw.isEmpty()) return null;

        // env:NAME -> value from environment variable (System.getenv)
        if (raw.startsWith("env:")) {
            String var = raw.substring(4);
            try {
                return System.getenv(var);
            } catch (Exception ex) {
                logger.debug("Error reading env var '{}': {}", var, ex.toString());
                return null;
            }
        }

        // spel:<expr> -> evaluate with SpEL (use variables with '#', e.g. #claims['sub'])
        if (raw.startsWith("spel:")) {
            String exprText = raw.substring(5);
            try {
                Expression expr = spelParser.parseExpression(exprText);
                Object val = expr.getValue(ctx);
                return val == null ? null : String.valueOf(val);
            } catch (Exception ex) {
                logger.debug("SpEL eval error for '{}': {}", exprText, ex.toString());
                return null;
            }
        }

        // literal (by default)
        return raw;
    }
}
