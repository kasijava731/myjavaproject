package com.e2s.cam.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Manager {
	private String value;
	private String $ref;
	private String displayName;
	public Manager(String value, String $ref, String displayName) {
		super();
		this.value = value;
		this.$ref = $ref;
		this.displayName = displayName;
	}
	public Manager() {
		super();
	}

}
