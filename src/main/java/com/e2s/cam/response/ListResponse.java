package com.e2s.cam.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ListResponse<T> {
	List<String> schemas = new ArrayList<String>();
	long totalResults;
	int startIndex;
	int itemsPerPage;
	List<T> resources = new ArrayList<T>();

	{
		schemas.add("urn:ietf:params:scim:api:messsages:2.0:ListResponse");
	}
	
	public List<String> getSchemas() {
		return schemas;
	}
	public void addToSchemas(String schema) {
		this.schemas.add(schema);
	}	
	
	public long getTotalResults() {
		return this.totalResults;
	}
	
	public void setTotalResults(long totalResults) {
		this.totalResults = totalResults;
	}
	
	@JsonProperty("Resources")
	public List<T> getResources() {
		return resources;
	}
	
	public void setResource(T t) {
		resources.add(t);
	}
	
	public int getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	public int getItemsPerPage() {
		return itemsPerPage;
	}
	public void setItemsPerPage(int itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}	
}