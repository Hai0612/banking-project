package com.bank.apigateway.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class JwtUtils {

    // NOTE: trong production nên inject từ config/secret manager
    private static final String SECRET = "change-this-secret-in-prod";

    public static Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token)
                .getBody();
    }

    public static String getUserId(String token) {
        return extractClaims(token).getSubject();
    }

    @SuppressWarnings("unchecked")
    public static List<String> getRoles(String token) {
        Object roles = extractClaims(token).get("roles");
        return roles != null ? (List<String>) roles : List.of();
    }

    public static boolean isExpired(String token) {
        return extractClaims(token).getExpiration().before(new java.util.Date());
    }
}
