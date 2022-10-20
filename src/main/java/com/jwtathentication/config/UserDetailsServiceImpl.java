package com.jwtathentication.config;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jwtathentication.entity.User;
import com.jwtathentication.repository.UserRepository;

@Service 
public class UserDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// fatch data by data base
		User user= userRepository.getUserByUserName(username);
		if(user==null) {
			throw new UsernameNotFoundException("could not found user");
		}else {
		   return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());	
		}
	}

}
