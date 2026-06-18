package com.sanosysalvos.gateway.filter;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @InjectMocks
    private JwtAuthenticationFilter filter;

    private GatewayFilterChain chain;

    @BeforeEach
    void setUp() {
        chain = mock(GatewayFilterChain.class);
        lenient().when(chain.filter(any(ServerWebExchange.class))).thenReturn(Mono.empty());
        ReflectionTestUtils.setField(filter, "jwtSecret", "sanosysalvos1234sanosysalvos1234sanosysalvos1234");
    }

    @Test
    void apply_RutaPublicaAuth_DeberiaPermitirAcceso() {
        MockServerHttpRequest request = MockServerHttpRequest.get("http://localhost/api/auth/login").build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);
        GatewayFilter gatewayFilter = filter.apply(new JwtAuthenticationFilter.Config());

        Mono<Void> result = gatewayFilter.filter(exchange, chain);

        StepVerifier.create(result).verifyComplete();
        verify(chain, times(1)).filter(exchange);
    }

    @Test
    void apply_FaltaCabeceraAuthorization_DeberiaRetornarUnauthorized() {
        MockServerHttpRequest request = MockServerHttpRequest.get("http://localhost/api/bff/pet/list").build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);
        GatewayFilter gatewayFilter = filter.apply(new JwtAuthenticationFilter.Config());

        Mono<Void> result = gatewayFilter.filter(exchange, chain);

        StepVerifier.create(result).verifyComplete();
        HttpStatus status = (HttpStatus) exchange.getResponse().getStatusCode();
        assertNotNull(status);
        assert(status.is4xxClientError());
    }

    @Test
    void apply_EstructuraTokenInvalida_DeberiaRetornarUnauthorized() {
        MockServerHttpRequest request = MockServerHttpRequest.get("http://localhost/api/bff/pet/list")
                .header(HttpHeaders.AUTHORIZATION, "Invalido token123")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);
        GatewayFilter gatewayFilter = filter.apply(new JwtAuthenticationFilter.Config());

        Mono<Void> result = gatewayFilter.filter(exchange, chain);

        StepVerifier.create(result).verifyComplete();
        HttpStatus status = (HttpStatus) exchange.getResponse().getStatusCode();
        assertNotNull(status);
        assert(status.is4xxClientError());
    }

    @Test
    void apply_TokenJwtInvalido_DeberiaRetornarUnauthorized() {
        MockServerHttpRequest request = MockServerHttpRequest.get("http://localhost/api/bff/pet/list")
                .header(HttpHeaders.AUTHORIZATION, "Bearer token.completamente.falso")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);
        GatewayFilter gatewayFilter = filter.apply(new JwtAuthenticationFilter.Config());

        Mono<Void> result = gatewayFilter.filter(exchange, chain);

        StepVerifier.create(result).verifyComplete();
        HttpStatus status = (HttpStatus) exchange.getResponse().getStatusCode();
        assertNotNull(status);
        assert(status.is4xxClientError());
    }
}