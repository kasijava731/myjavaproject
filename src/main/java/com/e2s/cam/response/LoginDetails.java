package com.e2s.cam.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDetails {

	private String lastLogin;
	private String lastPasswordChanged;
	private String isPrivileged;

}
