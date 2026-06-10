package com.bank.apigateway.ratelimit;


import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitService {

    // Simple in-memory fallback (useful for dev / fallback mode)
    private final Map<String, Integer> requestCounter = new ConcurrentHashMap<>();

    // default limits (can be replaced by Redis / config service)
    private static final int DEFAULT_LIMIT = 100;

    private static final int PAYMENT_LIMIT = 10;

    public boolean isAllowed(String key, String path) {

        int limit = resolveLimit(path);

        requestCounter.putIfAbsent(key, 0);

        int current = requestCounter.get(key);

        if (current >= limit) {
            return false;
        }

        requestCounter.put(key, current + 1);

        return true;
    }

    private int resolveLimit(String path) {

        // stricter for payment endpoints
        if (path.contains("/payments") || path.contains("/transfer")) {
            return PAYMENT_LIMIT;
        }

        return DEFAULT_LIMIT;
    }

    public void reset() {
        requestCounter.clear();
    }
}