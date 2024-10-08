package ru.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r.path("/auth/**")
                        .uri("http://localhost:8081"))
                .route("player-service", r -> r.path("/players/**")
                        .uri("http://localhost:8082"))
                .route("game-service", r -> r.path("/games/**")
                        .uri("http://localhost:8083"))
                .route("card-service", r -> r.path("/cards/**")
                        .uri("http://localhost:8084"))
                .route("session-service", r -> r.path("/sessions/**")
                        .uri("http://localhost:8085"))
                .route("notification-service", r -> r.path("/notifications/**")
                        .uri("http://localhost:8086"))
                .build();
    }
}
