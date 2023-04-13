package org.sid.securityservice.web.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginForm {

	private String username;
	private String password;
	private boolean withRefreshToken;
	private String grantType;
	private String refreshToken;
}
