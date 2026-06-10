package com.bank.apigateway.security;


import java.util.Map;

public class AuthenticationContext {

    private static final ThreadLocal<Map<String, Object>> CONTEXT = new ThreadLocal<>();

    public static void set(Map<String, Object> claims) {
        CONTEXT.set(claims);
    }

    public static Map<String, Object> get() {
        return CONTEXT.get();
    }

    public static String getUserId() {
        Map<String, Object> ctx = CONTEXT.get();
        return ctx != null ? (String) ctx.get("sub") : null;
    }

    public static void clear() {
        CONTEXT.remove();
    }
}