package com.apiGateway.APIGateway.Filters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@Slf4j
public class RequestFilter implements GlobalFilter{
    /**
     * This class is using global filter, which means it wil be called for all the requests. here we are
     * creating a new random id if it is not present already and gonna put in the request and pass it on to the
     * microservices. Check this commit to understand the changes i made to trace the flow.
     *
     * Here we are using reactive programing where mono and flux are used. mono is used when ever there is a
     * single value is returned and flux is used when more than one values are returned, and void or datatype type
     * is also used to say whether anything is returned or not....
     */
    public static final String correlationId = "correlation";
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders incomingRequestHeader = exchange.getRequest().getHeaders();
        if (incomingRequestHeader.get(RequestFilter.correlationId) != null)
        {
           log.info(incomingRequestHeader.get(RequestFilter.correlationId).toString());
        }
        else
        {
            String corelationid = UUID.randomUUID().toString();
            log.info("corelation id in request filter"+corelationid);

            exchange.getRequest().mutate().header(RequestFilter.correlationId, corelationid).build();
        }
        return chain.filter(exchange);
    }
}
