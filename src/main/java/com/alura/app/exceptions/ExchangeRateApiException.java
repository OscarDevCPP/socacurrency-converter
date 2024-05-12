package com.alura.app.exceptions;

public class ExchangeRateApiException extends RuntimeException {
	private final String error_type;

	public ExchangeRateApiException(String errorType) {
		this.error_type = errorType;
	}

	@Override
	public String getMessage() {
		return switch (error_type) {
			case "unsupported-code" ->
				"The given code isn't supported. See supported codes https://www.exchangerate-api.com/docs/supported-currencies ";
			case "malformed-request" -> "Bad endpoint, don't follow standard structure url for ExchangeRateApi.";
			case "invalid-key" -> "The given Api key isn't valid.";
			case "inactive-account" -> "Your email address wasn't confirmed. Please confirm your email address.";
			case "quota-reached" -> "Exceeded the number of requests allowed by your plan";
			default -> "Unknown Error: " + error_type;
		};
	}
}
