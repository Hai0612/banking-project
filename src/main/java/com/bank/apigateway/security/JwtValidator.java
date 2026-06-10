package com.bank.apigateway.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.stereotype.Component;

@Component
public class JwtValidator {

    private final JwtDecoder decoder = new JwtDecoder();

    public boolean validate(String token) {
        try {
            decoder.decode(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public Claims getClaims(String token) {
        return decoder.decode(token);
    }
}