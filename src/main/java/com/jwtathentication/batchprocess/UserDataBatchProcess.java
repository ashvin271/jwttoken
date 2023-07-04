package com.jwtathentication.batchprocess;

import org.springframework.batch.item.ItemProcessor;

import com.jwtathentication.entity.UserData;

public class UserDataBatchProcess implements ItemProcessor<UserData, UserData>{

	@Override
	public UserData process(UserData item) throws Exception {
		// TODO Auto-generated method stub
		return item;
	}

}
