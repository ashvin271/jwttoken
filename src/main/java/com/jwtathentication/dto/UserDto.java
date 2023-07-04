package com.jwtathentication.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

	  private Long id;
	  private String username;
	  private String firstName;
	  private String lastName;
	  private String role;
}
