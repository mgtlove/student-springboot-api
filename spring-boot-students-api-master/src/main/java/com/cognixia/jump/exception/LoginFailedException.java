package com.cognixia.jump.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class LoginFailedException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public LoginFailedException(String LoginErrorMessage) {
		super(LoginErrorMessage + ". Please try again.");
	}

}