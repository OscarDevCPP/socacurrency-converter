package com.alura.app;

import com.alura.app.models.Currency;
import com.alura.app.services.CurrencyServiceApi;
import com.alura.app.services.HistoryTransactionService;
import com.alura.app.utils.CoreHelpers;
import com.alura.app.views.ConverterCurrencyFrame;
import io.github.cdimascio.dotenv.Dotenv;

import javax.swing.JOptionPane;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Choquehuallpa Hurtado Oscar Alcides
 * 11 / 05 / 2024
 */
public class App  {

	public static File loadHistoryJsonFile() throws IOException {
		File file = new File("history.json");
		if(!file.exists()){
			var ignored = file.createNewFile();
		}
		return file;
	}

	public static void main(String[] args) {
		try {
			File historyJsonFile = loadHistoryJsonFile();
			HistoryTransactionService transactionService = new HistoryTransactionService(historyJsonFile);
			Dotenv dotenv = Dotenv.load();
			var pathToJson = CoreHelpers.getAssetFile("currencies.json");
			List<Currency> currencies = CoreHelpers.getListFromJson(pathToJson, Currency.class);
			String API_KEY = dotenv.get("API_KEY");
			ConverterCurrencyFrame frame = new ConverterCurrencyFrame(currencies, new CurrencyServiceApi(API_KEY), transactionService);
			frame.showWindow();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		// Crear un modelo o Record para un Historial
		// Documentar
	}
}
