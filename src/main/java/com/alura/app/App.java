package com.alura.app;

import com.alura.app.models.Currency;
import com.alura.app.services.CurrencyServiceApi;
import com.alura.app.utils.CoreHelpers;
import com.alura.app.views.ConverterCurrencyFrame;
import io.github.cdimascio.dotenv.Dotenv;

import javax.swing.JOptionPane;
import java.util.List;

/**
 * Hello world!
 */
public class App  {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		try {
			var pathToJson = CoreHelpers.getAssetFile("currencies.json");
			List<Currency> currencies = CoreHelpers.getListFromJson(pathToJson, Currency.class);
			String API_KEY = dotenv.get("API_KEY");
			ConverterCurrencyFrame frame = new ConverterCurrencyFrame(currencies, new CurrencyServiceApi(API_KEY));
			frame.showWindow();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		// Crear un modelo o Record para un Historial
		// Documentar
	}
}
