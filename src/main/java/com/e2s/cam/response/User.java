package com.e2s.cam.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
	List<String> schemas = new ArrayList<String>();
	String id;
	String externalId;
	Meta meta;
	String userName;
	String displayName;
	Name name;
	boolean active;
	List<Emails> emails;
	String profileUrl;
	String title;
	String userType;
	List<Groups> groups;	
	UserEnterprise enterprise;
	UserExtension extension;
	
	{
		schemas.add("urn:ietf:params:scim:schemas:core:2.0:User");
		schemas.add("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User");
		schemas.add("urn:ietf:params:scim:schemas:extension:cam:2.0:User");
	}
	
	public List<String> getSchemas() {
		return schemas;
	}
	public void addToSchemas(String schema) {
		this.schemas.add(schema);
	}	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getExternalId() {
		return externalId;
	}
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
	public Meta getMeta() {
		return meta;
	}
	public void setMeta(Meta meta) {
		this.meta = meta;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public Name getName() {
		return name;
	}
	public void setName(Name name) {
		this.name = name;
	}	
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public List<Emails> getEmails() {
		return emails;
	}
	public void setEmails(List<Emails> emails) {
		this.emails = emails;
	}

	public String getProfileUrl() {
		return profileUrl;
	}
	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public List<Groups> getGroups() {
		return groups;
	}
	public void setGroups(List<Groups> groups) {
		this.groups = groups;
	}
	@JsonProperty("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User")
	public UserEnterprise getEnterprise() {
		Manager manager=new Manager("","","");
		return	new UserEnterprise("","","",manager);
	}
	public void setEnterprise(UserEnterprise enterprise) {
		if (!this.getSchemas().contains("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User")) {
			this.getSchemas().add("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User");
		}
		this.enterprise = enterprise;
	}
	
	@JsonProperty("urn:ietf:params:scim:schemas:extension:cam:2.0:User")
	public UserExtension getExtension() {
		UserExtension userEx=new UserExtension();
		userEx.setIsPrivileged("");
		userEx.setLastLogin("True");
		userEx.setLastPasswordChanged("");
		return userEx;
	}
	public void setExtension(UserExtension extension) {
		if (!this.getSchemas().contains("urn:ietf:params:scim:schemas:extension:cam:2.0:User")) {
			this.getSchemas().add("urn:ietf:params:scim:schemas:extension:cam:2.0:User");
		}		
		this.extension = extension;
	}	
	
}