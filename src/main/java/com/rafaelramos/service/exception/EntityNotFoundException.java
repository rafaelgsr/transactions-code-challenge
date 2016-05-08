package com.rafaelramos.service.exception;

public class EntityNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 3157240785476487184L;
	
	public EntityNotFoundException() {
		super();
	}
	
	public EntityNotFoundException(String message) {
		super(message);
	}

}
