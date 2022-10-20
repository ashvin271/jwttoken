package com.jwtathentication;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
@EnableAspectJAutoProxy(proxyTargetClass=true)  
public class JwtathenticationApplication {
	
	public static final Logger LOGGER = LogManager.getLogger();

	public static void main(String[] args) {
		SpringApplication.run(JwtathenticationApplication.class, args);
	}

}
