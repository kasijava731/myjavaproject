package com.e2s.cam.response;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
public class UserExtension {
	
	DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	
	Date lastLogin;
	Date lastPasswordChanged;
	Boolean isPrivileged;
	
	public UserExtension() {
		utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	}
	
	public String getLastLogin() {
		return utcFormat.format(lastLogin);
	}
	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}
	public String getLastPasswordChanged() {
		return utcFormat.format(lastPasswordChanged);
	}
	public void setLastPasswordChanged(Date lastPasswordChanged) {
		this.lastPasswordChanged = lastPasswordChanged;
	}
	public Boolean getIsPrivileged() {
		return isPrivileged;
	}
	public void setIsPrivileged(Boolean isPrivileged) {
		this.isPrivileged = isPrivileged;
	}	
}