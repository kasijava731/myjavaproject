package com.e2s.cam.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Request {

	private String userId;
	private List<Operation> operations;
	private String filter;
	private int startIndex;
	private int itemsPerPage;
	private String ascOrderBy;
	private String descOrderBy;
	private String groupId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<Operation> getOperations() {
		return operations;
	}

	public void setOperations(List<Operation> operations) {
		this.operations = operations;
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
}
