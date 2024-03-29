package com.jwtathentication.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jwtathentication.entity.User;
import com.jwtathentication.service.UserService;
import com.jwtathentication.utility.ResponseMessage;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value="/api")
@Slf4j
public class UserController {
	
	@Autowired
	private UserService userService;
	

	@GetMapping("/getUser")
	public ResponseMessage<List<User>> getUsers(){
		log.info("get all users");
		return new ResponseMessage<>(HttpStatus.OK.value(), "Successfully Get All User",userService.getAllUsers());
	}
}
