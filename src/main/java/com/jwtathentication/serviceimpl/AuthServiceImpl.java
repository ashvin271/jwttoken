package com.jwtathentication.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jwtathentication.config.UserDetailsServiceImpl;
import com.jwtathentication.helper.JwtUtil;
import com.jwtathentication.payload.JwtAuthenticationResponse;
import com.jwtathentication.payload.LoginRequest;
import com.jwtathentication.service.AuthService;
import com.jwtathentication.service.UserService;

@Service
public class AuthServiceImpl implements AuthService{
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;  
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private UserService userService;

	@Override
	public JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest) {
		
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);
			UserDetails userDetails=userDetailsServiceImpl.loadUserByUsername(loginRequest.getEmail());
			String jwt = jwtUtil.generateToken(userDetails);
			String refreshToken = jwtUtil.generateRefreshToken(userDetails);
			
			return new JwtAuthenticationResponse(jwt, refreshToken, "Success", userService.findUserByName(userDetails.getUsername()));
		}catch (UsernameNotFoundException e) {
			throw new UsernameNotFoundException("Bed Credential !!");
		}
		
	}

}
