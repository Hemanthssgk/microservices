package com.apiGateway.APIGateway.Filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * We are using in the retun statement. here using then will ensure this filter
 * will only execute during the response. and once we get the response back. we are taking the
 * correlation id from the request and puting it to the response.
 */
@Component
@Slf4j
public class ResponseFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange).then(Mono.fromRunnable(
                () -> {
                    Optional<String> correlationId = exchange.getRequest().getHeaders().get(RequestFilter.correlationId).stream().findFirst();
                    exchange.getResponse().getHeaders().add(RequestFilter.correlationId, correlationId.get());
                    log.info("corelation id in response filter"+ exchange.getResponse().getHeaders().get(RequestFilter.correlationId));
                }
        ));
    }
}
