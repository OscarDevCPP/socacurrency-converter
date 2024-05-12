package com.alura.app.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

public record Transaction(
	String createdAt,
	String baseCurrencyCode,
	String targetCurrencyCode,
	String amount,
	String conversionResult
) {

	public static Transaction create(String base, String target, String amount, String result) {
		LocalDateTime currentDate = LocalDateTime.now();
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy");
		return new Transaction(currentDate.format(dateTimeFormatter), base, target, amount, result);
	}

	public Vector<String> getRowData(){
		Vector<String> rowData = new Vector<>();
		rowData.add(createdAt);
		rowData.add(String.format(" %s %s", amount, baseCurrencyCode));
		rowData.add(String.format(" %s %s", conversionResult, targetCurrencyCode));
		return rowData;
	}
}
