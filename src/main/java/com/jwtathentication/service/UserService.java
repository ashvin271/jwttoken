package com.jwtathentication.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;

import com.jwtathentication.entity.User;
import com.jwtathentication.payload.LoginRequest;

public interface UserService {
	
	public User findUserByName(String username);
	public User saveUser(User user);
	public List<User> getAllUsers();
	public ResponseEntity<?> getPdf(HttpServletRequest req, HttpServletResponse res);
}
