package com.alura.app.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record Transaction(
	String createdAt,
	String baseCurrencyCode,
	String targetCurrencyCode,
	String amount,
	String conversionResult
) {

	public static Transaction create(String base, String target, String amount, String result) {
		LocalDateTime currentDate = LocalDateTime.now();
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd, MM yyyy - HH:mm");
		return new Transaction(currentDate.format(dateTimeFormatter), base, target, amount, result);
	}
}
