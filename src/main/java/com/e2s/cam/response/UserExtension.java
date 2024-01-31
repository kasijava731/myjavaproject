package com.e2s.cam.response;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
public class UserExtension {
	
	DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	
//	Date lastLogin;
//	Date lastPasswordChanged;
//	Boolean isPrivileged;
	
	String lastLogin;
	String lastPasswordChanged;
	String isPrivileged;
	

	
	public String getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}
	public String getLastPasswordChanged() {
		return lastPasswordChanged;
	}
	public void setLastPasswordChanged(String lastPasswordChanged) {
		this.lastPasswordChanged = lastPasswordChanged;
	}
	public String getIsPrivileged() {
		return isPrivileged;
	}
	public void setIsPrivileged(String isPrivileged) {
		this.isPrivileged = isPrivileged;
	}
	
	
	
	
}