package com.infosys.retailer_service.exception;

public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = -3679202699186116937L;
	
	
	public NotFoundException(String message) {
		super(message);
	}
}
