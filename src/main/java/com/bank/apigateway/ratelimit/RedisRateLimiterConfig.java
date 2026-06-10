package com.bank.apigateway.ratelimit;


import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisRateLimiterConfig {

    /**
     * replenishRate = số request mỗi giây
     * burstCapacity = spike tối đa cho phép
     * requestedTokens = mỗi request consume bao nhiêu token
     */
    @Bean
    public RedisRateLimiter redisRateLimiter() {
        return new RedisRateLimiter(
                10,  // replenishRate (10 req/sec)
                20,  // burstCapacity
                1    // requestedTokens per request
        );
    }
}
