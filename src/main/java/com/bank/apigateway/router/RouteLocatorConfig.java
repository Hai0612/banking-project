package com.bank.apigateway.router;


import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteLocatorConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()

                // Auth
                .route("auth-service", r -> r.path("/api/auth/**")
                        .filters(f -> f.stripPrefix(0))
                        .uri("lb://AUTH-SERVICE"))

                .route("account-service", r -> r.path("/api/accounts/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://ACCOUNT-SERVICE"))

                .route("payment-service", r -> r.path("/api/payments/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://PAYMENT-SERVICE"))

                // Accounts
                .route("account-service", r -> r.path("/api/accounts/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("lb://ACCOUNT-SERVICE"))

                // Payment
                .route("payment-service", r -> r.path("/api/payments/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("lb://PAYMENT-SERVICE"))

                // Customer
                .route("customer-service", r -> r.path("/api/customers/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("lb://CUSTOMER-SERVICE"))

                // Transaction service
                .route("transaction-service", r -> r.path("/api/transactions/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("lb://TRANSACTION-SERVICE"))

                // Notification service
                .route("notification-service", r -> r.path("/api/notifications/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("lb://NOTIFICATION-SERVICE"))

                // Saga Orchestrator
                .route("saga-orchestrator-service", r -> r.path("/api/saga/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("lb://SAGA-ORCHESTRATOR-SERVICE"))

                .build();
    }
}