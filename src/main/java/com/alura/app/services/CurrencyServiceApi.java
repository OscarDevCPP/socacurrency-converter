package com.alura.app.services;

import com.alura.app.exceptions.ExchangeRateApiException;
import com.alura.app.utils.CoreHelpers;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.util.concurrent.CompletableFuture;

public class CurrencyServiceApi {
	private final String BASE_URL = "https://v6.exchangerate-api.com/v6";
	private final String apiKey;

	public CurrencyServiceApi(String apiKey) {
		this.apiKey = apiKey;
	}

	public CompletableFuture<Double> convert(
		double amount, String fromCurrencyCode, String toCurrencyCode
	) throws ParseException {
		String endpoint = CoreHelpers.makeApiUrl(
			BASE_URL, apiKey, "pair",
			fromCurrencyCode, toCurrencyCode,
			CoreHelpers.currencyFormat(amount)
		);

		return CoreHelpers.httpGetAsync(endpoint).thenCompose(response -> {
			JsonObject json = CoreHelpers.toJsonObject(response.body());
			String result = json.get("result").getAsString();
			if (result.equals("error")) {
				throw new ExchangeRateApiException(json.get("error-type").getAsString());
			}
			String conversionResult = json.get("conversion_result").getAsString();
			return CompletableFuture.supplyAsync(() -> {
				try {
					return CoreHelpers.currencyFormat(conversionResult);
				} catch (Exception e) {
					throw new RuntimeException("Error on convert: " + e.getMessage());
				}
			});
		});
	}

}
