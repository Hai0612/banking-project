package com.bank.apigateway.utils;


import org.springframework.http.server.reactive.ServerHttpRequest;

public class IpUtils {

    private static final String[] HEADERS_TO_CHECK = {
            "X-Forwarded-For",
            "X-Real-IP",
            "CF-Connecting-IP"
    };

    public static String getClientIp(ServerHttpRequest request) {

        for (String header : HEADERS_TO_CHECK) {
            String ip = request.getHeaders().getFirst(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                // X-Forwarded-For may contain multiple IPs
                return ip.split(",")[0].trim();
            }
        }

        if (request.getRemoteAddress() != null) {
            return request.getRemoteAddress()
                    .getAddress()
                    .getHostAddress();
        }

        return "UNKNOWN";
    }
}
