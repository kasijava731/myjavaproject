package com.e2s.cam.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GroupResponse {
	List<String> schemas = new ArrayList<String>();
	String id;
    String externalId;
    Meta meta;
    String displayName;
    List<Member> members;
    GroupExtension extension;
    
	{
		schemas.add("urn:ietf:params:scim:schemas:core:2.0:Group");
		schemas.add("urn:ietf:params:scim:schemas:extension:cam:2.0:Group");
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
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public List<Member> getMembers() {
		return members;
	}
	public void setMembers(List<Member> members) {
		this.members = members;
	} 
	@JsonProperty("urn:ietf:params:scim:schemas:extension:cam:2.0:Group")
	public GroupExtension getExtension() {
		return new GroupExtension("");
	}

	public void setExtension(GroupExtension extension) {
		if (!this.getSchemas().contains("urn:ietf:params:scim:schemas:extension:cam:2.0:Group")) {
			this.getSchemas().add("urn:ietf:params:scim:schemas:extension:cam:2.0:Group");
		}			
		this.extension = extension;
	}	
	
}