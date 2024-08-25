package com.apiGateway.APIGateway.Configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import java.time.Duration;
import java.util.Date;

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
                                .addResponseHeader("TimeResponseRecieved",new Date().toString()) // we can add a value to the header using this way.. we have many filters read the documentation... in this i also implemented one filter which will set correlation id..
                                .circuitBreaker(config -> config.setName("accountsCircuitBreaker").setFallbackUri("/fallback")))
                        .uri("lb://ACCOUNTS"))
                        .route(incomingPath -> incomingPath.path("/eazybank/cards/**")
                        .filters(filter -> filter.rewritePath("/eazybank/cards/(?<segment>.*)","/${segment}")
                                .retry(retryConfig -> retryConfig.setMethods(HttpMethod.GET).setRetries(3).setBackoff(Duration.ofMillis(100),Duration.ofMillis(200),2,true)))
                                .metadata("response-timeout", 1).metadata("connect-timeout",1)
                                .uri("lb://CARDS"))
                        .route(incomingPath -> incomingPath.path("/eazybank/loans/**")
                        .filters(filter -> filter.rewritePath("/eazybank/loans/(?<segment>.*)","/${segment}")).metadata("response-timeout", 1).metadata("connect-timeout",1)
                        .uri("lb://LOANS"))
                .build();
    }

//    usually when we use circuit breaker, by default there will be a default time limiter, so when ever we implement a retry pattern in the respective microservice
//    gateway wont wait to complete all the retries because circuit breaker will reach the time out and will release the connection. we can do two things.. one is to reduce the
//    timeout in the service or we need to change default timeout in the circuit breaker. we are doing that by below code...
    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> customizer()
    {
        return
                factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id).
                        circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                        .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build()).build());
    }
}
