package com.jwtathentication.payload;

import javax.validation.constraints.NotBlank;

/**
 * This class provides Authentication Parameter for Login.
 * 
 * @author ashvin
 *
 */
public class LoginRequest1 {

	@NotBlank(message = "Username can't be empty.")
	private String email;

	@NotBlank(message = "Password can't be empty.")
	private String password;


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public LoginRequest1() {
		super();
	}

	public LoginRequest1(String email,String password) {
		super();
		this.email = email;
		this.password = password;
	}

	@Override
	public String toString() {
		return "LoginRequest [email=" + email + ", password=" + password + "]";
	}

	
}
