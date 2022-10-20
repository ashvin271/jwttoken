package com.jwtathentication.service;

import com.jwtathentication.payload.JwtAuthenticationResponse;
import com.jwtathentication.payload.LoginRequest1;

public interface AuthService {

  public JwtAuthenticationResponse authenticateUser(LoginRequest1 loginRequest);
}
