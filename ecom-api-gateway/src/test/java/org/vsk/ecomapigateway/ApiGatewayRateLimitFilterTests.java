package org.vsk.ecomapigateway;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApiGatewayRateLimitFilterTests {

    @Test
    void allowsRequestWhenPermissionIsAvailable() {
        ApiGatewayRateLimitFilter filter = new ApiGatewayRateLimitFilter(rateLimiter());
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/users").build());
        AtomicBoolean chainInvoked = new AtomicBoolean();
        GatewayFilterChain chain = request -> {
            chainInvoked.set(true);
            return Mono.empty();
        };

        StepVerifier.create(filter.filter(exchange, chain)).verifyComplete();

        assertTrue(chainInvoked.get());
    }

    @Test
    void rejectsRequestWhenLimitIsExceeded() {
        RateLimiter rateLimiter = rateLimiter();
        rateLimiter.acquirePermission();
        ApiGatewayRateLimitFilter filter = new ApiGatewayRateLimitFilter(rateLimiter);
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/users").build());
        AtomicBoolean chainInvoked = new AtomicBoolean();
        GatewayFilterChain chain = request -> {
            chainInvoked.set(true);
            return Mono.empty();
        };

        StepVerifier.create(filter.filter(exchange, chain)).verifyComplete();

        assertEquals(HttpStatus.TOO_MANY_REQUESTS, exchange.getResponse().getStatusCode());
        assertFalse(chainInvoked.get());
    }

    private RateLimiter rateLimiter() {
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitForPeriod(1)
                .limitRefreshPeriod(Duration.ofHours(1))
                .timeoutDuration(Duration.ZERO)
                .build();
        return RateLimiter.of("test", config);
    }
}
