package com.apiGateway.APIGateway.Configuration;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Enabling custom routing instead of using default predicates..
 */
@Configuration
public class GatewayConfiguration {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder)
    {
        return routeLocatorBuilder
                .routes().route(incomingPath -> incomingPath.path("/eazybank/accounts/**")
                        .filters(filter -> filter.rewritePath("/eazybank/accounts/(?<segment>.*)","/${segment}")
                                .circuitBreaker(config -> config.setName("accountsCircuitBreaker").setFallbackUri("/fallback")))
                        .uri("lb://ACCOUNTS"))
                        .route(incomingPath -> incomingPath.path("/eazybank/cards/**")
                        .filters(filter -> filter.rewritePath("/eazybank/cards/(?<segment>.*)","/${segment}")).metadata("response-timeout", 1).metadata("connect-timeout",1)
                        .uri("lb://CARDS"))
                        .route(incomingPath -> incomingPath.path("/eazybank/loans/**")
                        .filters(filter -> filter.rewritePath("/eazybank/loans/(?<segment>.*)","/${segment}")).metadata("response-timeout", 1).metadata("connect-timeout",1)
                        .uri("lb://LOANS"))
                .build();
    }
}
