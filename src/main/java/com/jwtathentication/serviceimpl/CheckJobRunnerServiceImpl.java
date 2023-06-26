package com.jwtathentication.serviceimpl;

import org.springframework.stereotype.Service;

import com.jwtathentication.service.CheckJobRunnerService;

@Service
public class CheckJobRunnerServiceImpl implements CheckJobRunnerService{

	@Override
	public void getstart() {
		
		for (int i = 0; i < 20; i++) {
			try {
				Thread.sleep(2000);
				 System.out.println("task ==="+i);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
