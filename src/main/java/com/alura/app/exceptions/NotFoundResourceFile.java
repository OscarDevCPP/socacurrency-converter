package com.alura.app.exceptions;

public class NotFoundResourceFile extends Exception {
	public final String message;

	public NotFoundResourceFile(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return "Not found resource file: " + message + ", please check filename in your resources folder.";
	}
}
