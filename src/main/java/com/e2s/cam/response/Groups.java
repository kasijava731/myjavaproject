package com.e2s.cam.response;

import com.fasterxml.jackson.annotation.JsonProperty;



public class Groups {
	private String value;
	private String sref;
	private String display;
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	@JsonProperty("$ref")
	public String getSref() {
		return sref;
	}
	public void setSref(String sref) {
		this.sref = sref;
	}
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
	

}
