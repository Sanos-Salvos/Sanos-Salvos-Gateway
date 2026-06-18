package com.sanosysalvos.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"spring.cloud.gateway.enabled=false"
})
class GatewayApplicationTests {

	@Test
	void contextLoads() {
	}

}