package com.infosys.exception;

public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = -3679202699186116937L;
	
	private String message;
	
	public NotFoundException(String message) {
		super(message);
		this.message = message;
	}
}
