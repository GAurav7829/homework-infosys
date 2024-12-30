package com.infosys.exception;

public class InvalidMonthException extends RuntimeException {

	private static final long serialVersionUID = -6555786619233662448L;
	
	public InvalidMonthException(String message) {
		super(message);
	}

}
