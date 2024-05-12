package com.alura.app.services;

import com.alura.app.models.Transaction;
import com.alura.app.utils.CoreHelpers;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HistoryTransactionService {
	private final File jsonFile;

	public HistoryTransactionService(File jsonFile)  {
		this.jsonFile = jsonFile;
	}

	public List<Transaction> getTransactions() throws IOException {
		createJsonFileIfNotExists();
		return CoreHelpers.getListFromJson(jsonFile.getAbsolutePath(), Transaction.class);
	}

	public void save(Transaction newTransaction) throws IOException {
		createJsonFileIfNotExists();
		var transactions = getTransactions();
		transactions.add(newTransaction);
		try(FileWriter writer = new FileWriter(jsonFile)) {
			Gson gson = new GsonBuilder()
				.setPrettyPrinting()
				.create();
			writer.write(gson.toJson(transactions));
		}
	}

	public void clearAll() throws IOException {
		createJsonFileIfNotExists();
		try(FileWriter writer = new FileWriter(jsonFile)) {
			Gson gson = new Gson();
			writer.write(gson.toJson(new ArrayList<Transaction>()));
		}
	}

	private void createJsonFileIfNotExists() throws IOException {
		if(!jsonFile.exists()){
			var ignored = jsonFile.createNewFile();
			try(FileWriter writer = new FileWriter(jsonFile)){
				writer.write("[]");
			}
		}
	}

}
