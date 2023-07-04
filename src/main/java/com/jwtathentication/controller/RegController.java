package com.jwtathentication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jwtathentication.entity.User;
import com.jwtathentication.service.UserService;
import com.jwtathentication.utility.ResponseMessage;

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
	public ResponseMessage<User> saveUser(@RequestBody User user) {
		return new ResponseMessage<>(HttpStatus.OK.value(), "Successfully Created",
				userService.saveUser(user));
	}
	
}
