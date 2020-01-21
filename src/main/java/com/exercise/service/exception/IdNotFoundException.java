package com.exercise.service.exception;

import java.util.ArrayList;
import java.util.List;

public class IdNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private List<String> errors = new ArrayList<>();

	public IdNotFoundException(List<String> errors) {
		this.errors = errors;
	}
	
	public List<String >getErrors() {
		return errors;
	}

}
