package com.e2s.cam.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Member {
	
	private String value;
	private String sref;
	private String type;
	@JsonProperty("$ref")
	public String getSref() {
		return "";
	}
	public void setSref(String sref) {
		this.sref = sref;
	}
}
