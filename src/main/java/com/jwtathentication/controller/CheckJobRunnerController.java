package com.jwtathentication.controller;

import java.time.LocalDateTime;

import org.jobrunr.scheduling.JobScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jwtathentication.service.CheckJobRunnerService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/task")
@Slf4j
public class CheckJobRunnerController {
	
	@Autowired
	private JobScheduler jobScheduler;
	
	@Autowired
	private CheckJobRunnerService checkJobRunnerService;

	private long countRequest;

	
	@GetMapping("/runing")
	public ResponseEntity<String> getLocaleReportNew() {
		log.info("start countRequest :{}", countRequest);
		int SCHEDULER_INTERVAL_REQUEST=0;
		int scheduleNextTask = 0;
		if (countRequest > 0) {
			scheduleNextTask = SCHEDULER_INTERVAL_REQUEST;
		}

		jobScheduler.schedule(LocalDateTime.now().plusMinutes(scheduleNextTask), () -> {
			checkJobRunnerService.getstart();
		});
		countRequest++;

		return new ResponseEntity<>("job enqueued successfully", HttpStatus.OK);
	}

}
