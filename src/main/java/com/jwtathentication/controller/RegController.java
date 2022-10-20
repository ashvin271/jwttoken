package com.jwtathentication.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jwtathentication.bean.FarmarBean;
import com.jwtathentication.bean.Survey;
import com.jwtathentication.entity.User;
import com.jwtathentication.payload.LoginRequest;
import com.jwtathentication.service.UserService;
import com.jwtathentication.utility.ResponseMessage1;

@RestController
@RequestMapping(value="/register") 
public class RegController {
	
	@Autowired
	private UserService userService;

	@GetMapping("/")
	public String home() {
		return "this is my home page";
	}
	
	@PostMapping("/save")
	public ResponseMessage1<User> saveUser(@RequestBody User user) {
		return new ResponseMessage1<>(HttpStatus.OK.value(), "Successfully Created",
				userService.saveUser(user));
	}
	
	
	@RequestMapping(value = "/get_token", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> getToken(@RequestBody LoginRequest loginRequest) throws Exception {
		String result = userService.getToken(loginRequest);
		System.out.println("report generate successfuly......");
		return new ResponseEntity<>(result, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/save_survey_form", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<String>> surveyDetails(@RequestBody Survey survey) throws Exception {
		List<String> result = userService.surveyDetails(survey);
		System.out.println("save survey form......");
		return new ResponseEntity<List<String>>(result, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/get_reports", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Map<String,Object>> getSurveyReport(@RequestParam long farmid) throws Exception {
		Map<String,Object> result = userService.getSurveyReport(farmid);
		System.out.println("getting survey report.successfuly.....");
		return new ResponseEntity<Map<String,Object>>(result,HttpStatus.OK);
		
	}

	@RequestMapping(value = "/save_survey_form2", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Map<String,Object>>> surveyDetails2(@RequestBody Survey survey) throws Exception {
		List<Map<String,Object>> result = userService.surveyDetails2(survey);
		System.out.println("save survey form......");
		return new ResponseEntity<List<Map<String,Object>>>(result, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/get_farmer_data_by_fid", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String,Object>> getFarmerDataByFID(@RequestBody FarmarBean farmarBean) throws Exception {
		Map<String,Object> result = userService.getFarmerDataByFID(farmarBean);
		System.out.println("get farmer data by fid......");
		return new ResponseEntity<Map<String,Object>>(result, HttpStatus.OK);
		
	}
	
}
