package com.jwtathentication.service;

import com.jwtathentication.payload.JwtAuthenticationResponse;
import com.jwtathentication.payload.LoginRequest;

public interface AuthService {

  public JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest);
}
