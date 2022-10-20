package com.jwtathentication.service;

import java.util.List;
import java.util.Map;

import com.jwtathentication.bean.FarmarBean;
import com.jwtathentication.bean.Survey;
import com.jwtathentication.entity.User;
import com.jwtathentication.payload.LoginRequest;

public interface UserService {
	
	public User findUserByName(String username);
	public User saveUser(User user);
	public List<User> getAllUsers();
	public String getToken(LoginRequest loginRequest) throws Exception;
	public List<String> surveyDetails(Survey survey) throws Exception;
	public Map<String,Object> getSurveyReport(long farmid) throws Exception;
	public List<Map<String,Object>> surveyDetails2(Survey survey) throws Exception;
	public Map<String,Object> getFarmerDataByFID(FarmarBean farmarBean) throws Exception;
}
