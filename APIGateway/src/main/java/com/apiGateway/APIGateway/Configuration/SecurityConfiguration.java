package com.apiGateway.APIGateway.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity // if it is not webflux use the @EnableWebSecurity.
public class SecurityConfiguration {
// all the code snippets are taken from the spring security documentation. https://docs.spring.io/spring-security/site/docs/5.2.1.RELEASE/reference/htmlsingle/#oauth2resourceserver-jwt-authorization-extraction
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) // since reactive gateway so we are using serverHttpSecurity instead oƒ httpsecurity.
    {
        serverHttpSecurity.authorizeExchange( exchange -> exchange.pathMatchers(HttpMethod.GET).permitAll()); // all the get method call we are permitting, not securing them.
        serverHttpSecurity.authorizeExchange(
                        exchange -> exchange
                                .pathMatchers("/eazybank/accounts/**").hasRole("ACCOUNTS") // all the other calls with account prefix will be authenticated..
                                .pathMatchers("/eazybank/cards/**").hasRole("CARDS")
                                .pathMatchers("/eazybank/loans/**").hasRole("LOANS")
                )
                .oauth2ResourceServer(oAuth2ResourceServerSpec -> oAuth2ResourceServerSpec.jwt(jwtSpec -> jwtSpec.jwtAuthenticationConverter(grantedAuthoritiesExtractor())));
        serverHttpSecurity.csrf(ServerHttpSecurity.CsrfSpec::disable); // disabling csrf becuase it is not required when browsers are not involved. But if we have browser, we need to config
        return serverHttpSecurity.build();
//        serverHttpSecurity.authorizeExchange( exchange -> exchange.pathMatchers(HttpMethod.GET).permitAll()); // all the get method call we are permitting, not securing them.
//        serverHttpSecurity.authorizeExchange(
//                exchange -> exchange
//                        .pathMatchers("/eazybank/accounts/**").authenticated() // all the other calls with account prefix will be authenticated..
//                        .pathMatchers("/eazybank/cards/**").authenticated()
//                        .pathMatchers("/eazybank/loans/**").authenticated()
//                )
//                .oauth2ResourceServer(oAuth2ResourceServerSpec -> oAuth2ResourceServerSpec.jwt(Customizer.withDefaults()));
//        serverHttpSecurity.csrf(ServerHttpSecurity.CsrfSpec::disable); // disabling csrf becuase it is not required when browsers are not involved. But if we have browser, we need to config
//        return serverHttpSecurity.build();
    }
// this config is taken from https://docs.spring.io/spring-security/site/docs/5.2.1.RELEASE/reference/htmlsingle/#webflux-oauth2resourceserver-jwt-authorization-extraction
    Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
        JwtAuthenticationConverter jwtAuthenticationConverter =
                new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter
                (new JWTRoleExtractor());
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }
}
