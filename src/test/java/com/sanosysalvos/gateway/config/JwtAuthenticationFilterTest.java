package com.sanosysalvos.gateway.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private static final String SECRET = "this-is-a-very-long-secret-key-for-jwt-signing-purpose-1234";
    private SecretKey key;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockFilterChain filterChain;

    @BeforeEach
    void setUp() {
        key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = new MockFilterChain();
        SecurityContextHolder.clearContext();
    }

    private String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(key)
                .compact();
    }

    @Test
    void validToken_setsAuthentication() throws Exception {
        String token = generateToken("nicolas");
        request.addHeader("Authorization", "Bearer " + token);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("nicolas", SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Test
    void missingAuthorizationHeader_passesThrough() throws Exception {
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(200, response.getStatus());
    }

    @Test
    void invalidToken_returns401() throws Exception {
        request.addHeader("Authorization", "Bearer invalid.token.value");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertEquals(401, response.getStatus());
    }

    @Test
    void expiredToken_returns401() throws Exception {
        String expiredToken = Jwts.builder()
                .subject("nicolas")
                .issuedAt(new Date(System.currentTimeMillis() - 7200000))
                .expiration(new Date(System.currentTimeMillis() - 3600000))
                .signWith(key)
                .compact();
        request.addHeader("Authorization", "Bearer " + expiredToken);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertEquals(401, response.getStatus());
    }

    @Test
    void nonBearerPrefix_passesThrough() throws Exception {
        request.addHeader("Authorization", "Basic dXNlcjpwYXNz");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void emptyBearerToken_returns401() throws Exception {
        request.addHeader("Authorization", "Bearer ");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertEquals(401, response.getStatus());
    }

    @Test
    void validToken_filterChainContinues() throws Exception {
        String token = generateToken("nicolas");
        request.addHeader("Authorization", "Bearer " + token);

        MockFilterChain chain = new MockFilterChain();
        jwtAuthenticationFilter.doFilterInternal(request, response, chain);

        assertNotNull(chain.getRequest());
    }
}
