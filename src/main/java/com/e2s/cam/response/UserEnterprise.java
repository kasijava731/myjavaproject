package com.e2s.cam.response;
public class UserEnterprise {
	String organization;
	String division;
	String department;
	Manager manager;
	
	public Manager getManager() {
		return manager;
	}
	public void setManager(Manager manager) {
		this.manager = manager;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public String getDivision() {
		return division;
	}
	public void setDivision(String division) {
		this.division = division;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public UserEnterprise(String organization, String division, String department, Manager manager) {
		super();
		this.organization = organization;
		this.division = division;
		this.department = department;
		this.manager = manager;
	}
	public UserEnterprise() {
		super();
	}	
	
}