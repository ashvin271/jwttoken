package com.jwtathentication;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({"test"})
@PropertySource("classpath:application-test.properties")
public class JwtathenticationApplicationTest {

	@Test
	void contextLoads() {
	}
}
