package org.vsk.ecomapigateway;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimiterConfiguration {

    @Bean
    public RateLimiter apiGatewayRateLimiter(
            @Value("${gateway.rate-limit.limit-for-period:100}") int limitForPeriod,
            @Value("${gateway.rate-limit.limit-refresh-period:1s}") Duration limitRefreshPeriod) {
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitForPeriod(limitForPeriod)
                .limitRefreshPeriod(limitRefreshPeriod)
                .timeoutDuration(Duration.ZERO)
                .build();
        return RateLimiter.of("api-gateway", config);
    }
}
