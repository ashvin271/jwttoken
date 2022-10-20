package com.jwtathentication.payload;

import javax.validation.constraints.NotBlank;

public class LoginRequest {

	@NotBlank(message = "grant Type can't be empty.")
	private String grant_type;
	
	@NotBlank(message = "Username can't be empty.")
	private String username;

	@NotBlank(message = "Password can't be empty.")
	private String password;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGrant_type() {
		return grant_type;
	}

	public void setGrant_type(String grant_type) {
		this.grant_type = grant_type;
	}
	
}
