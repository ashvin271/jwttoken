package com.jwtathentication.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jwtathentication.payload.JwtAuthenticationResponse;
import com.jwtathentication.payload.LoginRequest1;
import com.jwtathentication.service.AuthService;
import com.jwtathentication.utility.ResponseMessage1;

@RestController
@CrossOrigin
public class AuthController {

	@Autowired
	private AuthService authService;
	
	@PostMapping("/token")
	public ResponseMessage1<JwtAuthenticationResponse>  authenticateUser(@Valid  @RequestBody LoginRequest1 request){
		return new ResponseMessage1<>(HttpStatus.OK.value(), "Successfully login",
				authService.authenticateUser(request));
	}
}
