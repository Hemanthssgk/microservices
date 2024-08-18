package com.apiGateway.APIGateway.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity // if it is not webflux use the @EnableWebSecurity.
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity)
    {
        serverHttpSecurity.authorizeExchange( exchange -> exchange.pathMatchers(HttpMethod.GET).permitAll()); // all the get method call we are permitting, not securing them.
        serverHttpSecurity.authorizeExchange(
                exchange -> exchange
                        .pathMatchers("/eazybank/accounts/**").authenticated() // all the other calls with account prefix will be authenticated..
                        .pathMatchers("/eazybank/cards/**").authenticated()
                        .pathMatchers("/eazybank/loans/**").authenticated()
                )
                .oauth2ResourceServer(oAuth2ResourceServerSpec -> oAuth2ResourceServerSpec.jwt(Customizer.withDefaults()));
        serverHttpSecurity.csrf(ServerHttpSecurity.CsrfSpec::disable); // disabling csrf becuase it is not required when browsers are not involved. But if we have browser, we need to config
        return serverHttpSecurity.build();

    }
}
