package org.sid.securityservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class UserForm {
	
	private String username;
	
	private String password;
	
	private String confirmPassword;

}
