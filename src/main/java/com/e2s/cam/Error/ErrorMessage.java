package com.e2s.cam.Error;

import java.util.List;

public class ErrorMessage {
	List<String> schemas;
	String details;
	int status;
	public List<String> getSchemas() {
		return schemas;
	}
	public void setSchemas(List<String> schemas) {
		this.schemas = schemas;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public ErrorMessage(List<String> schemas, String details, int status) {
		super();
		this.schemas = schemas;
		this.details = details;
		this.status = status;
	}
	public ErrorMessage() {
		super();
	}
	

}
