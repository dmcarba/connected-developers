package com.exercise.service.exception;

import java.util.List;

public class ErrorMessage {
	private List<String> errors;

	ErrorMessage(List<String> errors) {
		this.setErrors(errors);
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
}