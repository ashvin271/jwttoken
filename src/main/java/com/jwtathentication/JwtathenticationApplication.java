package com.jwtathentication;

import org.jobrunr.jobs.mappers.JobMapper;
import org.jobrunr.storage.InMemoryStorageProvider;
import org.jobrunr.storage.StorageProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
//@EnableAspectJAutoProxy(proxyTargetClass=true)
public class JwtathenticationApplication {
	
	private final Logger log = LoggerFactory.getLogger(JwtathenticationApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(JwtathenticationApplication.class, args);
	}
	
	@Bean
	public StorageProvider storageProvider(JobMapper jobMapper) {
	    InMemoryStorageProvider storageProvider = new InMemoryStorageProvider();
	    storageProvider.setJobMapper(jobMapper);
	    return storageProvider;
	}

}
