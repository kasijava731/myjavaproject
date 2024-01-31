package com.e2s.cam.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;




public class Request {

	private String userId;
	@JsonProperty("Operations")
	private List<Operation> Operations;
	private String filter;
	private int startIndex;
	private int itemsPerPage;
	private String ascOrderBy;
	private String descOrderBy;
	private String groupId;

	public List<Operation> getOperations() {
		return Operations;
	}

	public void setOperations(List<Operation> operations) {
		Operations = operations;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
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

	public String getAscOrderBy() {
		return ascOrderBy;
	}

	public void setAscOrderBy(String ascOrderBy) {
		this.ascOrderBy = ascOrderBy;
	}

	public String getDescOrderBy() {
		return descOrderBy;
	}

	public void setDescOrderBy(String descOrderBy) {
		this.descOrderBy = descOrderBy;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	@Override
	public String toString() {
		return "Request [userId=" + userId + ", Operations=" + Operations + ", filter=" + filter + ", startIndex="
				+ startIndex + ", itemsPerPage=" + itemsPerPage + ", ascOrderBy=" + ascOrderBy + ", descOrderBy="
				+ descOrderBy + ", groupId=" + groupId + "]";
	}
	
	
}
