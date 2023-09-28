package com.e2s.cam.module;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ErrorResponseMessage {
	
	private static final Logger logger = LogManager.getLogger(ErrorResponseMessage.class);
	
	List<String> schemas = new ArrayList<String>();
	String detail;
	String status;
	
	{
		schemas.add("urn:ietf:params:scim:api:messages:2.0:Error");
	}
	
	public ErrorResponseMessage(String detail, String status) {
		this.detail = detail;
		this.status = status;
		logger.error(detail);
	}
	
	public List<String> getSchemas() {
		return schemas;
	}
	public void setSchemas(List<String> schemas) {
		this.schemas = schemas;
	}		

	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}	
}
