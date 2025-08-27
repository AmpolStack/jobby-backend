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

/**
 * DynamicHeaderGatewayFilterFactory
 * <p>
 * A GatewayFilterFactory for Spring Cloud Gateway that injects multiple dynamic headers
 * into proxied requests. Configuration is per-route via {@code application.yml}.
 * </p>
 *
 * <h3>Summary</h3>
 * <p>
 * - Supports a list of header definitions (name/value) to inject into the request forwarded to the backend.
 * - Value formats supported: *   <ul>
 *     <li><b>Literal</b> — e.g. {@code some-literal-value}.</li>
 *     <li><b>Env</b> — {@code env:VAR_NAME} reads from {@code System.getenv("VAR_NAME")}.</li>
 *     <li><b>SpEL</b> — {@code spel:<expression>} evaluates a SpEL expression with a predefined evaluation context (see <em>SpEL Context</em>).</li>
 *   </ul>
 * </p>
 *
 * <h3>SpEL evaluation context</h3>
 * <p>
 * When evaluating a SpEL expression, the following variables are available in the evaluation context:
 * </p>
 * <ul>
 *   <li>{@code #claims} — a {@code Map<String,Object>} containing JWT claims when a {@code JwtAuthenticationToken} is present; otherwise an empty map.</li>
 *   <li>{@code #principal} — the {@link org.springframework.security.core.Authentication} instance (maybe {@code null}).</li>
 *   <li>{@code #request} — the {@link org.springframework.http.server.reactive.ServerHttpRequest} for the incoming request.</li>
 *   <li>{@code #exchange} — the {@link org.springframework.web.server.ServerWebExchange} instance.</li>
 * </ul>
 *
 * <h3>Example (application.yml)</h3>
 * <pre>
 * spring:
 *   cloud: *     gateway: *       routes: *         - id: example-service *           uri: example-uri *           predicates: *             - Path=/api/users/** *           filters: *             - name: DynamicHeader *               args: *                 headers[0].name: X-User-Id *                 headers[0].value: "spel:#claims['sub']" *                 headers[1].name: X-User-Email *                 headers[1].value: "spel:#claims['email']" *                 headers[2].name: X-Env *                 headers[2].value: env:MY_ENV_VAR *                 headers[3].name: X-Literal *                 headers[3].value: some-literal-value * </pre>
 *
 * <h3>Behavior when authentication or claims are missing</h3>
 * <ul>
 *   <li>If there is no authentication, {@code #claims} is an empty map; SpEL expressions that reference missing claims return {@code null} and the header is not added.</li>
 *   <li>If a SpEL evaluation throws an exception, the error is logged at DEBUG level and the header is not added (the filter will not produce a 500 error due to a failing SpEL expression).</li>
 * </ul>
 *
 * <h3>Security considerations</h3>
 * <ul>
 *   <li>The filter removes any incoming headers that would be added (prevents client spoofing).</li>
 *   <li>Do not expose sensitive data (e.g. raw tokens, secrets) as headers.</li>
 *   <li>If you require extra integrity guarantees, consider signing internal headers (for example, {@code X-Gateway-Signature}) and verifying them in the backend.</li>
 * </ul>
 *
 * <h3>Performance & maintenance notes</h3>
 * <ul>
 *   <li>SpEL evaluation has a runtime cost; avoid extremely complex SpEL expressions on very hot routes or consider caching computed values if feasible.</li>
 *   <li>The filter is stateless and safe for concurrent use within the Gateway runtime.</li>
 * </ul>
 *
 * <h3>Recommended usage</h3>
 * <ol>
 *   <li>Use {@code spel:#claims['sub']} (note the {@code #}) to reference injected variables in SpEL.</li>
 *   <li>Test SpEL expressions in staging and enable DEBUG logs if a value does not resolve as expected.</li>
 *   <li>Apply this filter only on routes that actually need the dynamic headers to avoid unnecessary header propagation.</li>
 * </ol>
 *
 * @since 1.0.0
 * @see org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
 * @see org.springframework.expression.spel.standard.SpelExpressionParser
 */
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
