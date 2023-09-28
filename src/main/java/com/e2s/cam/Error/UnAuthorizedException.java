package com.e2s.cam.Error;
public class UnAuthorizedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UnAuthorizedException(String msg) {
		super(msg);
	}

}