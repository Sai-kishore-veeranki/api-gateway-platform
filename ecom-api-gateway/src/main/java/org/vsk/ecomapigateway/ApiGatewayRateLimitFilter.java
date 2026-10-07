package org.vsk.ecomapigateway;

import io.github.resilience4j.ratelimiter.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class ApiGatewayRateLimitFilter implements GlobalFilter {

    private static final Logger log = LoggerFactory.getLogger(ApiGatewayRateLimitFilter.class);
    private final RateLimiter rateLimiter;

    public ApiGatewayRateLimitFilter(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        if (rateLimiter.acquirePermission()) {
            log.debug("Gateway request allowed for {}", path);
            return chain.filter(exchange);
        }

        log.warn("Rate limit exceeded for {}", path);
        exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
        return exchange.getResponse().setComplete();
    }
}
