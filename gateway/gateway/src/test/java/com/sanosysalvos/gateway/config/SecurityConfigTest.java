package com.sanosysalvos.gateway.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = SecurityConfig.class)
class SecurityConfigTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void springSecurityFilterChain_DeberiaRegistrarseComoBean() {
        SecurityWebFilterChain chain = context.getBean(SecurityWebFilterChain.class);

        assertNotNull(chain);
    }
}