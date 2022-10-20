package com.jwtathentication.payload;

import com.jwtathentication.entity.User;

public class JwtAuthenticationResponse {

	private String accessToken;
	private String refreshToken;
	private String tokenType = "Bearer";
	private String message;
	private User user;
	
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public String getTokenType() {
		return tokenType;
	}
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	public JwtAuthenticationResponse() {
		super();
	}
	public JwtAuthenticationResponse(String accessToken, String refreshToken, String message,
			User user) {
		super();
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.message = message;
		this.user = user;
	}
	
	
	
}
