package com.jwtathentication.batchprocess;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jwtathentication.repository.BatchProcessRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JobCompletionListener extends JobExecutionListenerSupport {

	    private final BatchProcessRepository batchProcessRepository;

	    @Autowired
	    public JobCompletionListener(BatchProcessRepository batchProcessRepository) {
	        this.batchProcessRepository = batchProcessRepository;
	    }

	    // The callback method from the Spring Batch JobExecutionListenerSupport class that is executed when the batch process is completed
	    @Override
	    public void afterJob(JobExecution jobExecution) {
	        // When the batch process is completed the the users in the database are retrieved and logged on the application logs
	        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
	        log.info("!!! JOB COMPLETED! verify the results");
	        batchProcessRepository.findAll()
	        .forEach(person -> log.info("Found (" + person + ">) in the database.") );
	        }
	    }
}
