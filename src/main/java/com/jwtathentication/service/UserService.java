package com.jwtathentication.service;

import java.util.List;

import com.jwtathentication.entity.User;
import com.jwtathentication.payload.LoginRequest;

public interface UserService {
	
	public User findUserByName(String username);
	public User saveUser(User user);
	public List<User> getAllUsers();
}
