package com.shopping.portal.exception;

public class CustomApplicationException extends RuntimeException {
	private final int status;

	public CustomApplicationException(int status, String message) {
		super(message);
		this.status = status;
	}

	public int getStatus() {
		return status;
	}
}